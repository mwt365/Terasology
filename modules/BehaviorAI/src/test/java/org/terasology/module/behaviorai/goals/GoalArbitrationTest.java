// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.goals;

import org.junit.jupiter.api.Test;
import org.terasology.module.behaviorai.components.GoalComponent;
import org.terasology.module.behaviorai.components.MemoryComponent;
import org.terasology.module.behaviorai.nodes.Navigator;
import org.terasology.module.behaviorai.systems.AIArbiter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Exercises the survive-and-gather scenario at the arbitration layer using
 * a stub navigator that teleports instantly, so we can observe goal
 * selection without a world.
 */
class GoalArbitrationTest {

    private static final Navigator INSTANT_NAV = (actor, x, y, z, delta) -> true;

    private GoalComponent goalsWithSafetyAndGather() {
        GoalComponent goals = new GoalComponent();
        goals.addGoal(new IdleGoal());
        goals.addGoal(new GatherResourceGoal(INSTANT_NAV));
        goals.addGoal(new FleeGoal(INSTANT_NAV));
        return goals;
    }

    @Test
    void idleWhenNothingInMemory() {
        GoalComponent goals = goalsWithSafetyAndGather();
        MemoryComponent memory = new MemoryComponent();

        AIArbiter.tick(new Object(), goals, memory, 0.1f, null);
        assertNotNull(goals.activeGoal);
        assertEquals("idle", goals.activeGoal.getName());
    }

    @Test
    void gatherWhenResourceVisible() {
        GoalComponent goals = goalsWithSafetyAndGather();
        MemoryComponent memory = new MemoryComponent();
        memory.rememberResource(new MemoryComponent.Percept("tree", 1, 0, 1, 0f));

        AIArbiter.tick(new Object(), goals, memory, 0.1f, null);
        assertEquals("gather", goals.activeGoal.getName());
    }

    @Test
    void safetyOverridesGatherWhenThreatAppears() {
        GoalComponent goals = goalsWithSafetyAndGather();
        MemoryComponent memory = new MemoryComponent();
        memory.rememberResource(new MemoryComponent.Percept("tree", 1, 0, 1, 0f));
        memory.rememberThreat(new MemoryComponent.Percept("wolf", 5, 0, 5, 0f));

        AIArbiter.tick(new Object(), goals, memory, 0.1f, null);
        assertEquals("flee", goals.activeGoal.getName());
    }
}
