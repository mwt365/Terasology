// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.events;

import org.terasology.gestalt.entitysystem.event.Event;
import org.terasology.module.advancednpcai.model.Goal;

public class GoalCompletedEvent implements Event {
    private final Goal completedGoal;

    public GoalCompletedEvent(Goal completedGoal) {
        this.completedGoal = completedGoal;
    }

    public Goal getCompletedGoal() {
        return completedGoal;
    }
}
