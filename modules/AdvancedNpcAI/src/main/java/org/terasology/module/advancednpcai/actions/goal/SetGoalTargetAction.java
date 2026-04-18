// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.goal;

import org.joml.Vector3f;
import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;

@BehaviorAction(name = "set_goal_target")
public class SetGoalTargetAction extends BaseAction {
    private String sourceKey = "foundResourcePosition";

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        Vector3f pos = actor.readFromBlackboard(sourceKey);
        if (pos != null) {
            actor.writeToBlackboard("moveTarget", pos);
            return BehaviorState.SUCCESS;
        }
        return BehaviorState.FAILURE;
    }
}
