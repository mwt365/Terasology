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
import org.terasology.module.advancednpcai.events.GoalCompletedEvent;
import org.terasology.module.advancednpcai.model.Goal;
import org.terasology.module.advancednpcai.model.GoalType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationEnvironment(dependencies = "AdvancedNpcAI")
public class GoalSystemTest {
    @In
    protected EntityManager entityManager;
    @In
    protected ModuleTestingHelper helper;
    @In
    protected GoalSystem goalSystem;

    private EntityRef createNpc() {
        EntityRef npc = entityManager.create();
        npc.addComponent(new AIComponent());
        npc.addComponent(new GoalComponent());
        npc.addComponent(new MemoryComponent());
        return npc;
    }

    @Test
    public void testGoalPrioritySorting() {
        EntityRef npc = createNpc();
        goalSystem.addGoal(npc, new Goal(GoalType.IDLE, 0));
        goalSystem.addGoal(npc, new Goal(GoalType.GATHER_RESOURCE, 50));
        goalSystem.addGoal(npc, new Goal(GoalType.FLEE_THREAT, 100));

        GoalComponent gc = npc.getComponent(GoalComponent.class);
        assertNotNull(gc.getActiveGoal());
        assertEquals(GoalType.FLEE_THREAT, gc.getActiveGoal().type);
        assertEquals(3, gc.goals.size());
    }

    @Test
    public void testGoalCompletion() {
        EntityRef npc = createNpc();
        try (TestEventReceiver<GoalCompletedEvent> receiver = new TestEventReceiver<>(
                helper.getHostContext(), GoalCompletedEvent.class)) {
            goalSystem.addGoal(npc, new Goal(GoalType.GATHER_RESOURCE, 50));
            goalSystem.completeActiveGoal(npc);

            assertEquals(1, receiver.getEvents().size());
            assertTrue(npc.getComponent(GoalComponent.class).goals.get(0).completed);
        }
    }

    @Test
    public void testMaxGoalsEviction() {
        EntityRef npc = createNpc();
        GoalComponent gc = npc.getComponent(GoalComponent.class);
        gc.maxGoals = 3;
        npc.saveComponent(gc);

        goalSystem.addGoal(npc, new Goal(GoalType.IDLE, 0));
        goalSystem.addGoal(npc, new Goal(GoalType.EXPLORE, 10));
        goalSystem.addGoal(npc, new Goal(GoalType.GATHER_RESOURCE, 50));
        goalSystem.addGoal(npc, new Goal(GoalType.FLEE_THREAT, 100));

        gc = npc.getComponent(GoalComponent.class);
        assertEquals(3, gc.goals.size());
        assertEquals(GoalType.FLEE_THREAT, gc.getActiveGoal().type);
    }

    @Test
    public void testIdleGoalAddedWhenQueueEmpty() {
        EntityRef npc = createNpc();
        goalSystem.addGoal(npc, new Goal(GoalType.GATHER_RESOURCE, 50));
        goalSystem.completeActiveGoal(npc);

        GoalComponent gc = npc.getComponent(GoalComponent.class);
        Goal active = gc.getActiveGoal();
        assertNotNull(active);
        assertEquals(GoalType.IDLE, active.type);
    }

    @Test
    public void testPruneCompletedGoals() {
        EntityRef npc = createNpc();
        goalSystem.addGoal(npc, new Goal(GoalType.IDLE, 0));
        goalSystem.addGoal(npc, new Goal(GoalType.GATHER_RESOURCE, 50));
        goalSystem.completeActiveGoal(npc);

        goalSystem.pruneCompletedGoals(npc);
        GoalComponent gc = npc.getComponent(GoalComponent.class);
        for (Goal g : gc.goals) {
            assertTrue(!g.completed || g.type == GoalType.IDLE);
        }
    }
}
