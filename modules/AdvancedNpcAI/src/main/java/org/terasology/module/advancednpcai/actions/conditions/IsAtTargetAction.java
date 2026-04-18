// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.conditions;

import org.joml.Vector3f;
import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.engine.logic.location.LocationComponent;

@BehaviorAction(name = "is_at_target")
public class IsAtTargetAction extends BaseAction {
    private float arrivalDistance = 1.5f;

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        LocationComponent loc = actor.getComponent(LocationComponent.class);
        Vector3f target = actor.readFromBlackboard("moveTarget");
        if (loc == null || target == null) {
            return BehaviorState.FAILURE;
        }
        return loc.getLocalPosition().distance(target) <= arrivalDistance
                ? BehaviorState.SUCCESS : BehaviorState.FAILURE;
    }
}
