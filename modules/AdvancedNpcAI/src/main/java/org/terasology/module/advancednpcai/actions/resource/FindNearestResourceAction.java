// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.resource;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.WorldProvider;
import org.terasology.engine.world.block.Block;
import org.terasology.module.advancednpcai.events.ResourceFoundEvent;

@BehaviorAction(name = "find_nearest_resource")
public class FindNearestResourceAction extends BaseAction {
    private String blockUri;
    private float searchRadius = 15f;

    @In
    private transient WorldProvider worldProvider;

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        String targetUri = blockUri;
        String bbUri = actor.readFromBlackboard("goalTargetBlockUri");
        if (bbUri != null) {
            targetUri = bbUri;
        }
        if (targetUri == null || worldProvider == null) {
            return BehaviorState.FAILURE;
        }

        LocationComponent loc = actor.getComponent(LocationComponent.class);
        if (loc == null) {
            return BehaviorState.FAILURE;
        }

        org.joml.Vector3fc pos = loc.getLocalPosition();
        Vector3f bestPos = null;
        float bestDist = Float.MAX_VALUE;

        int r = (int) searchRadius;
        for (int x = -r; x <= r; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -r; z <= r; z++) {
                    Vector3i checkPos = new Vector3i(
                            (int) pos.x() + x, (int) pos.y() + y, (int) pos.z() + z);
                    Block block = worldProvider.getBlock(checkPos);
                    if (block != null && block.getURI().toString().equals(targetUri)) {
                        float dist = pos.distance(checkPos.x, checkPos.y, checkPos.z);
                        if (dist < bestDist) {
                            bestDist = dist;
                            bestPos = new Vector3f(checkPos.x, checkPos.y, checkPos.z);
                        }
                    }
                }
            }
        }

        if (bestPos != null) {
            actor.writeToBlackboard("moveTarget", bestPos);
            actor.writeToBlackboard("foundResourcePosition", bestPos);
            actor.writeToBlackboard("foundResourceBlockUri", targetUri);
            actor.getEntity().send(new ResourceFoundEvent(targetUri, bestPos));
            return BehaviorState.SUCCESS;
        }
        return BehaviorState.FAILURE;
    }
}
