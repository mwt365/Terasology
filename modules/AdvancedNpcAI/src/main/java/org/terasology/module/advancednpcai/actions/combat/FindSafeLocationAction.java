// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.combat;

import org.joml.Vector3f;
import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.utilities.random.FastRandom;
import org.terasology.engine.utilities.random.Random;

@BehaviorAction(name = "find_safe_location")
public class FindSafeLocationAction extends BaseAction {
    private float safeDistance = 20f;

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        LocationComponent loc = actor.getComponent(LocationComponent.class);
        if (loc == null) {
            return BehaviorState.FAILURE;
        }

        Vector3f currentPos = new Vector3f(loc.getLocalPosition());
        Vector3f threatPos = actor.readFromBlackboard("threatPosition");

        Vector3f safePos;
        if (threatPos != null) {
            Vector3f fleeDir = new Vector3f(currentPos).sub(threatPos).normalize();
            safePos = new Vector3f(currentPos).add(
                    fleeDir.x * safeDistance, 0, fleeDir.z * safeDistance);
        } else {
            Random random = new FastRandom();
            float angle = random.nextFloat() * 2f * (float) Math.PI;
            safePos = new Vector3f(
                    currentPos.x + (float) Math.cos(angle) * safeDistance,
                    currentPos.y,
                    currentPos.z + (float) Math.sin(angle) * safeDistance);
        }

        actor.writeToBlackboard("moveTarget", safePos);
        return BehaviorState.SUCCESS;
    }
}
