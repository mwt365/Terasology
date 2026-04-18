// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.goal;

import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;

@BehaviorAction(name = "select_highest_priority_goal")
public class SelectHighestPriorityGoalAction extends BaseAction {
    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        String goalType = actor.readFromBlackboard("activeGoalType");
        if (goalType != null) {
            return BehaviorState.SUCCESS;
        }
        return BehaviorState.FAILURE;
    }
}
