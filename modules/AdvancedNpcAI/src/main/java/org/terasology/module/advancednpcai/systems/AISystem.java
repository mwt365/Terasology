// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnAddedComponent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.behavior.BehaviorComponent;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.module.advancednpcai.components.AIComponent;
import org.terasology.module.advancednpcai.components.GoalComponent;
import org.terasology.module.advancednpcai.components.MemoryComponent;
import org.terasology.module.advancednpcai.events.NpcSpawnedEvent;
import org.terasology.module.advancednpcai.model.Goal;
import org.terasology.module.advancednpcai.model.ThreatLevel;

@RegisterSystem(RegisterMode.AUTHORITY)
@Share(AISystem.class)
public class AISystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    private static final Logger logger = LoggerFactory.getLogger(AISystem.class);

    @In
    private EntityManager entityManager;

    @ReceiveEvent
    public void onAIAdded(OnAddedComponent event, EntityRef entity, AIComponent ai) {
        if (!entity.hasComponent(GoalComponent.class)) {
            entity.addComponent(new GoalComponent());
        }
        if (!entity.hasComponent(MemoryComponent.class)) {
            entity.addComponent(new MemoryComponent());
        }
        entity.send(new NpcSpawnedEvent());
    }

    @Override
    public void update(float delta) {
        for (EntityRef entity : entityManager.getEntitiesWith(AIComponent.class, GoalComponent.class)) {
            AIComponent ai = entity.getComponent(AIComponent.class);
            if (!ai.active) {
                continue;
            }

            ai.timeSinceLastEvaluation += delta;
            if (ai.timeSinceLastEvaluation >= ai.reevaluationInterval) {
                ai.timeSinceLastEvaluation = 0f;
                syncGoalToBlackboard(entity);
            }
        }
    }

    private void syncGoalToBlackboard(EntityRef entity) {
        BehaviorComponent bc = entity.getComponent(BehaviorComponent.class);
        if (bc == null || bc.interpreter == null) {
            return;
        }
        GoalComponent gc = entity.getComponent(GoalComponent.class);
        if (gc == null) {
            return;
        }

        Actor actor = bc.interpreter.actor();
        Goal activeGoal = gc.getActiveGoal();
        if (activeGoal != null) {
            actor.writeToBlackboard("activeGoalType", activeGoal.type.name());
            actor.writeToBlackboard("activeGoalPriority", activeGoal.priority);
            if (activeGoal.targetPosition != null) {
                actor.writeToBlackboard("goalTargetPosition", activeGoal.targetPosition);
            }
            if (activeGoal.targetBlockUri != null) {
                actor.writeToBlackboard("goalTargetBlockUri", activeGoal.targetBlockUri);
            }
            actor.writeToBlackboard("goalTargetEntity", activeGoal.targetEntity);
        } else {
            actor.writeToBlackboard("activeGoalType", null);
        }

        AIComponent ai = entity.getComponent(AIComponent.class);
        actor.writeToBlackboard("threatLevel", ai.currentThreatLevel.name());
        MemoryComponent mc = entity.getComponent(MemoryComponent.class);
        if (mc != null && mc.lastThreatPosition != null) {
            actor.writeToBlackboard("threatPosition", mc.lastThreatPosition);
            actor.writeToBlackboard("threatEntity", mc.threatEntity);
        }
    }
}
