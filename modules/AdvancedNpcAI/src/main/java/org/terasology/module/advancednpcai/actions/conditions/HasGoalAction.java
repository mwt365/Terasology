// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.conditions;

import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;

@BehaviorAction(name = "has_goal")
public class HasGoalAction extends BaseAction {
    private String goalType;

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        String activeGoalType = actor.readFromBlackboard("activeGoalType");
        if (activeGoalType == null) {
            return BehaviorState.FAILURE;
        }
        if (goalType != null && !goalType.equals(activeGoalType)) {
            return BehaviorState.FAILURE;
        }
        return BehaviorState.SUCCESS;
    }
}
