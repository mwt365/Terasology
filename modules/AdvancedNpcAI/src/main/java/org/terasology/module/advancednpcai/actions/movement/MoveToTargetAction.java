// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.movement;

import org.joml.Vector3f;
import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.engine.logic.location.LocationComponent;

@BehaviorAction(name = "move_to_target")
public class MoveToTargetAction extends BaseAction {
    private float arrivalDistance = 1.5f;
    private String targetKey = "moveTarget";
    private float moveSpeed = 1.0f;

    @Override
    public void construct(Actor actor) {
        actor.setValue(getId(), Boolean.TRUE);
    }

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        Vector3f target = actor.readFromBlackboard(targetKey);
        if (target == null) {
            return BehaviorState.FAILURE;
        }

        LocationComponent loc = actor.getComponent(LocationComponent.class);
        if (loc == null) {
            return BehaviorState.FAILURE;
        }

        Vector3f currentPos = new Vector3f(loc.getLocalPosition());
        float distance = currentPos.distance(target);

        if (distance <= arrivalDistance) {
            return BehaviorState.SUCCESS;
        }

        Vector3f direction = new Vector3f(target).sub(currentPos).normalize();
        float step = moveSpeed * actor.getDelta();
        if (step > distance) {
            step = distance;
        }

        Vector3f newPos = new Vector3f(currentPos).add(
                direction.x * step, direction.y * step, direction.z * step);
        loc.position.set(newPos);
        actor.save(loc);

        return BehaviorState.RUNNING;
    }

    @Override
    public void destruct(Actor actor) {
        actor.setValue(getId(), null);
    }
}
