// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.components;

import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.module.behaviorai.goals.Goal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the goals an NPC is willing to pursue. The {@code AIUpdateSystem}
 * picks the goal with the highest utility every tick and runs its tree.
 *
 * Goals are free to score themselves from the {@link MemoryComponent}.
 */
public class GoalComponent implements Component<GoalComponent> {
    public transient List<Goal> goals = new ArrayList<>();

    /** The goal active on the previous tick (used to detect switches). */
    public transient Goal activeGoal;

    public GoalComponent addGoal(Goal goal) {
        goals.add(goal);
        return this;
    }

    public List<Goal> getGoals() {
        return Collections.unmodifiableList(goals);
    }

    @Override
    public void copyFrom(GoalComponent other) {
        this.goals = new ArrayList<>(other.goals);
        this.activeGoal = other.activeGoal;
    }
}
