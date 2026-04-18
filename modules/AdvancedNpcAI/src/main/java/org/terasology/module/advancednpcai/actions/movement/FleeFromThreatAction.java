// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.movement;

import org.joml.Vector3f;
import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.engine.logic.location.LocationComponent;

@BehaviorAction(name = "flee_from_threat")
public class FleeFromThreatAction extends BaseAction {
    private float fleeDistance = 15f;
    private float fleeSpeed = 2.5f;

    @Override
    public void construct(Actor actor) {
        Vector3f threatPos = actor.readFromBlackboard("threatPosition");
        LocationComponent loc = actor.getComponent(LocationComponent.class);
        if (threatPos != null && loc != null) {
            Vector3f currentPos = new Vector3f(loc.getLocalPosition());
            Vector3f fleeDir = new Vector3f(currentPos).sub(threatPos).normalize();
            Vector3f fleeTarget = new Vector3f(currentPos).add(
                    fleeDir.x * fleeDistance, 0, fleeDir.z * fleeDistance);
            actor.setValue(getId(), fleeTarget);
        }
    }

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        Vector3f fleeTarget = actor.getValue(getId());
        if (fleeTarget == null) {
            return BehaviorState.FAILURE;
        }

        LocationComponent loc = actor.getComponent(LocationComponent.class);
        if (loc == null) {
            return BehaviorState.FAILURE;
        }

        Vector3f currentPos = new Vector3f(loc.getLocalPosition());
        float distance = currentPos.distance(fleeTarget);
        if (distance <= 1.5f) {
            return BehaviorState.SUCCESS;
        }

        Vector3f direction = new Vector3f(fleeTarget).sub(currentPos).normalize();
        float step = fleeSpeed * actor.getDelta();
        if (step > distance) {
            step = distance;
        }

        Vector3f newPos = new Vector3f(currentPos).add(
                direction.x * step, 0, direction.z * step);
        loc.position.set(newPos);
        actor.save(loc);

        return BehaviorState.RUNNING;
    }
}
