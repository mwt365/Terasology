// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.systems;

import org.junit.jupiter.api.Test;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.integrationenvironment.ModuleTestingHelper;
import org.terasology.engine.integrationenvironment.TestEventReceiver;
import org.terasology.engine.integrationenvironment.jupiter.IntegrationEnvironment;
import org.terasology.engine.registry.In;
import org.terasology.module.advancednpcai.components.AIComponent;
import org.terasology.module.advancednpcai.components.GoalComponent;
import org.terasology.module.advancednpcai.components.MemoryComponent;
import org.terasology.module.advancednpcai.events.NpcSpawnedEvent;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationEnvironment(dependencies = "AdvancedNpcAI")
public class AISystemTest {
    @In
    protected EntityManager entityManager;
    @In
    protected ModuleTestingHelper helper;

    @Test
    public void testAIComponentAutoAddsGoalAndMemory() {
        EntityRef npc = entityManager.create();
        npc.addComponent(new AIComponent());

        assertNotNull(npc.getComponent(GoalComponent.class));
        assertNotNull(npc.getComponent(MemoryComponent.class));
    }

    @Test
    public void testNpcSpawnedEventFired() {
        try (TestEventReceiver<NpcSpawnedEvent> receiver = new TestEventReceiver<>(
                helper.getHostContext(), NpcSpawnedEvent.class)) {
            EntityRef npc = entityManager.create();
            npc.addComponent(new AIComponent());

            assertTrue(receiver.getEvents().size() >= 1);
        }
    }

    @Test
    public void testExistingGoalComponentNotOverwritten() {
        EntityRef npc = entityManager.create();
        GoalComponent gc = new GoalComponent();
        gc.maxGoals = 5;
        npc.addComponent(gc);
        npc.addComponent(new AIComponent());

        assertEquals(5, npc.getComponent(GoalComponent.class).maxGoals);
    }

    private void assertEquals(int expected, int actual) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }
}
