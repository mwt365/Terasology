// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.systems;

import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.characters.AliveCharacterComponent;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.module.advancednpcai.components.AIComponent;
import org.terasology.module.advancednpcai.components.GoalComponent;
import org.terasology.module.advancednpcai.components.MemoryComponent;
import org.terasology.module.advancednpcai.events.ThreatClearedEvent;
import org.terasology.module.advancednpcai.events.ThreatDetectedEvent;
import org.terasology.module.advancednpcai.model.Goal;
import org.terasology.module.advancednpcai.model.GoalType;
import org.terasology.module.advancednpcai.model.ThreatLevel;

@RegisterSystem(RegisterMode.AUTHORITY)
@Share(ThreatSystem.class)
public class ThreatSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    private static final Logger logger = LoggerFactory.getLogger(ThreatSystem.class);
    private static final float SCAN_INTERVAL = 1.0f;

    @In
    private EntityManager entityManager;
    @In
    private Time time;

    private float timeSinceLastScan = 0f;

    @Override
    public void update(float delta) {
        timeSinceLastScan += delta;
        if (timeSinceLastScan < SCAN_INTERVAL) {
            return;
        }
        timeSinceLastScan = 0f;

        for (EntityRef npc : entityManager.getEntitiesWith(AIComponent.class, LocationComponent.class)) {
            AIComponent ai = npc.getComponent(AIComponent.class);
            if (!ai.active) {
                continue;
            }

            LocationComponent npcLoc = npc.getComponent(LocationComponent.class);
            Vector3f npcPos = npcLoc.getLocalPosition();

            ThreatLevel maxThreat = ThreatLevel.NONE;
            EntityRef closestThreat = EntityRef.NULL;
            Vector3f closestThreatPos = null;

            for (EntityRef other : entityManager.getEntitiesWith(
                    AliveCharacterComponent.class, LocationComponent.class)) {
                if (other.equals(npc)) {
                    continue;
                }
                if (other.hasComponent(AIComponent.class)) {
                    continue;
                }
                LocationComponent otherLoc = other.getComponent(LocationComponent.class);
                Vector3f otherPos = otherLoc.getLocalPosition();
                float dist = npcPos.distance(otherPos);
                if (dist <= ai.detectionRange) {
                    ThreatLevel level;
                    if (dist < ai.detectionRange / 3f) {
                        level = ThreatLevel.HIGH;
                    } else if (dist < ai.detectionRange * 2f / 3f) {
                        level = ThreatLevel.MEDIUM;
                    } else {
                        level = ThreatLevel.LOW;
                    }
                    if (level.getSeverity() > maxThreat.getSeverity()) {
                        maxThreat = level;
                        closestThreat = other;
                        closestThreatPos = new Vector3f(otherPos);
                    }
                }
            }

            ThreatLevel previousThreat = ai.currentThreatLevel;
            ai.currentThreatLevel = maxThreat;
            npc.saveComponent(ai);

            if (maxThreat.getSeverity() > ThreatLevel.NONE.getSeverity()
                    && previousThreat.getSeverity() < maxThreat.getSeverity()) {
                MemoryComponent mc = npc.getComponent(MemoryComponent.class);
                if (mc != null) {
                    mc.threatEntity = closestThreat;
                    mc.lastThreatPosition = closestThreatPos;
                    npc.saveComponent(mc);
                }
                npc.send(new ThreatDetectedEvent(closestThreat, closestThreatPos, maxThreat));
            } else if (maxThreat == ThreatLevel.NONE && previousThreat != ThreatLevel.NONE) {
                MemoryComponent mc = npc.getComponent(MemoryComponent.class);
                if (mc != null) {
                    mc.threatEntity = EntityRef.NULL;
                    mc.lastThreatPosition = null;
                    npc.saveComponent(mc);
                }
                npc.send(new ThreatClearedEvent());
            }
        }
    }

    @ReceiveEvent(components = {AIComponent.class, GoalComponent.class})
    public void onThreatDetected(ThreatDetectedEvent event, EntityRef entity) {
        if (event.getLevel().getSeverity() >= ThreatLevel.HIGH.getSeverity()) {
            GoalSystem goalSystem = CoreRegistry.get(GoalSystem.class);
            if (goalSystem != null) {
                Goal fleeGoal = new Goal(GoalType.FLEE_THREAT, 100);
                fleeGoal.targetPosition = event.getThreatPosition();
                fleeGoal.targetEntity = event.getThreatEntity();
                goalSystem.addGoal(entity, fleeGoal);
            }
        }
    }

    @ReceiveEvent(components = {AIComponent.class, GoalComponent.class})
    public void onThreatCleared(ThreatClearedEvent event, EntityRef entity) {
        GoalComponent gc = entity.getComponent(GoalComponent.class);
        if (gc != null) {
            gc.goals.removeIf(g -> g.type == GoalType.FLEE_THREAT && !g.completed);
            entity.saveComponent(gc);
        }
    }
}
