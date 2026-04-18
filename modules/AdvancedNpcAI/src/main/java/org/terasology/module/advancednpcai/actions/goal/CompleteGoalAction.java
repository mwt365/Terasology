// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.goal;

import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.registry.In;
import org.terasology.module.advancednpcai.systems.GoalSystem;

@BehaviorAction(name = "complete_goal")
public class CompleteGoalAction extends BaseAction {
    @In
    private transient GoalSystem goalSystem;

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        GoalSystem gs = goalSystem;
        if (gs == null) {
            gs = CoreRegistry.get(GoalSystem.class);
        }
        if (gs != null) {
            gs.completeActiveGoal(actor.getEntity());
            return BehaviorState.SUCCESS;
        }
        return BehaviorState.FAILURE;
    }
}
