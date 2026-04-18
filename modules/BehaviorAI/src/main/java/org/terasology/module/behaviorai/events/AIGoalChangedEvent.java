// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.events;

import org.terasology.gestalt.entitysystem.event.Event;

/**
 * Emitted when an entity's active goal transitions to a different goal.
 * Carries the previous and new goal names so observers (UI, tests, logging)
 * can react without peeking at internal state.
 */
public class AIGoalChangedEvent implements Event {
    private final String previousGoal;
    private final String newGoal;

    public AIGoalChangedEvent(String previousGoal, String newGoal) {
        this.previousGoal = previousGoal;
        this.newGoal = newGoal;
    }

    public String getPreviousGoal() {
        return previousGoal;
    }

    public String getNewGoal() {
        return newGoal;
    }
}
