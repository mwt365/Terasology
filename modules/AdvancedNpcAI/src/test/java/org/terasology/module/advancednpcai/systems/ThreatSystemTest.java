// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.systems;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.integrationenvironment.ModuleTestingHelper;
import org.terasology.engine.integrationenvironment.TestEventReceiver;
import org.terasology.engine.integrationenvironment.jupiter.IntegrationEnvironment;
import org.terasology.engine.logic.characters.AliveCharacterComponent;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.module.advancednpcai.components.AIComponent;
import org.terasology.module.advancednpcai.components.GoalComponent;
import org.terasology.module.advancednpcai.components.MemoryComponent;
import org.terasology.module.advancednpcai.events.ThreatDetectedEvent;
import org.terasology.module.advancednpcai.model.ThreatLevel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationEnvironment(dependencies = "AdvancedNpcAI")
public class ThreatSystemTest {
    @In
    protected EntityManager entityManager;
    @In
    protected ModuleTestingHelper helper;

    private EntityRef createNpc(Vector3f position) {
        EntityRef npc = entityManager.create();
        npc.addComponent(new LocationComponent(position));
        npc.addComponent(new AIComponent());
        npc.addComponent(new GoalComponent());
        npc.addComponent(new MemoryComponent());
        return npc;
    }

    private EntityRef createPlayer(Vector3f position) {
        EntityRef player = entityManager.create();
        player.addComponent(new LocationComponent(position));
        player.addComponent(new AliveCharacterComponent());
        return player;
    }

    @Test
    public void testThreatDetectionWithinRange() {
        try (TestEventReceiver<ThreatDetectedEvent> receiver = new TestEventReceiver<>(
                helper.getHostContext(), ThreatDetectedEvent.class)) {
            EntityRef npc = createNpc(new Vector3f(0, 0, 0));
            createPlayer(new Vector3f(5, 0, 0));

            helper.runUntil(() -> !receiver.getEvents().isEmpty());

            assertNotNull(receiver.getEvents().get(0));
            assertEquals(ThreatLevel.HIGH, receiver.getEvents().get(0).getLevel());
        }
    }

    @Test
    public void testNoThreatOutsideRange() {
        try (TestEventReceiver<ThreatDetectedEvent> receiver = new TestEventReceiver<>(
                helper.getHostContext(), ThreatDetectedEvent.class)) {
            EntityRef npc = createNpc(new Vector3f(0, 0, 0));
            AIComponent ai = npc.getComponent(AIComponent.class);
            ai.detectionRange = 10f;
            npc.saveComponent(ai);

            createPlayer(new Vector3f(50, 0, 0));

            helper.runWhile(() -> receiver.getEvents().isEmpty());
            assertEquals(0, receiver.getEvents().size());
        }
    }
}
