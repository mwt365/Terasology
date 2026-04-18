// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.events;

import org.terasology.gestalt.entitysystem.event.Event;
import org.terasology.module.advancednpcai.model.Goal;

public class GoalChangedEvent implements Event {
    private final Goal previousGoal;
    private final Goal newGoal;

    public GoalChangedEvent(Goal previousGoal, Goal newGoal) {
        this.previousGoal = previousGoal;
        this.newGoal = newGoal;
    }

    public Goal getPreviousGoal() {
        return previousGoal;
    }

    public Goal getNewGoal() {
        return newGoal;
    }
}
