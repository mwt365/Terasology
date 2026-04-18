// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.components;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import org.terasology.module.advancednpcai.model.Goal;
import org.terasology.module.advancednpcai.model.GoalType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GoalComponentTest {

    @Test
    public void testGetActiveGoalReturnsHighestPriorityNonCompleted() {
        GoalComponent gc = new GoalComponent();
        Goal highPriority = new Goal(GoalType.FLEE_THREAT, 100);
        Goal lowPriority = new Goal(GoalType.IDLE, 0);

        gc.goals.add(highPriority);
        gc.goals.add(lowPriority);

        assertEquals(GoalType.FLEE_THREAT, gc.getActiveGoal().type);
    }

    @Test
    public void testGetActiveGoalSkipsCompleted() {
        GoalComponent gc = new GoalComponent();
        Goal completed = new Goal(GoalType.FLEE_THREAT, 100);
        completed.completed = true;
        Goal active = new Goal(GoalType.IDLE, 0);

        gc.goals.add(completed);
        gc.goals.add(active);

        assertEquals(GoalType.IDLE, gc.getActiveGoal().type);
    }

    @Test
    public void testGetActiveGoalReturnsNullWhenAllCompleted() {
        GoalComponent gc = new GoalComponent();
        Goal g = new Goal(GoalType.IDLE, 0);
        g.completed = true;
        gc.goals.add(g);

        assertNull(gc.getActiveGoal());
    }

    @Test
    public void testGetActiveGoalReturnsNullWhenEmpty() {
        GoalComponent gc = new GoalComponent();
        assertNull(gc.getActiveGoal());
    }

    @Test
    public void testCopyFromDeepCopiesGoals() {
        GoalComponent source = new GoalComponent();
        Goal goal = new Goal(GoalType.GATHER_RESOURCE, 50);
        goal.targetPosition = new Vector3f(1, 2, 3);
        goal.targetBlockUri = "CoreAssets:Stone";
        source.goals.add(goal);
        source.maxGoals = 5;

        GoalComponent target = new GoalComponent();
        target.copyFrom(source);

        assertEquals(1, target.goals.size());
        assertEquals(5, target.maxGoals);
        assertEquals(GoalType.GATHER_RESOURCE, target.goals.get(0).type);
        assertEquals(50, target.goals.get(0).priority);
        assertNotSame(source.goals.get(0), target.goals.get(0));
        assertNotSame(source.goals.get(0).targetPosition, target.goals.get(0).targetPosition);
    }
}
