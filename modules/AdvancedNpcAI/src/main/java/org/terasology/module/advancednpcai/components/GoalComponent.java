// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.components;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.module.advancednpcai.model.Goal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoalComponent implements Component<GoalComponent> {
    @Replicate
    public List<Goal> goals = new ArrayList<>();

    @Replicate
    public int maxGoals = 10;

    @Override
    public void copyFrom(GoalComponent other) {
        this.goals = other.goals.stream().map(Goal::copy).collect(Collectors.toList());
        this.maxGoals = other.maxGoals;
    }

    public Goal getActiveGoal() {
        for (Goal g : goals) {
            if (!g.completed) {
                return g;
            }
        }
        return null;
    }
}
