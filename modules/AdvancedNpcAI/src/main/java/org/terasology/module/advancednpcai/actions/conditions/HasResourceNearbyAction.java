// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.conditions;

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

@BehaviorAction(name = "has_resource_nearby")
public class HasResourceNearbyAction extends BaseAction {
    private String blockUri;
    private float searchRadius = 10f;

    @In
    private transient WorldProvider worldProvider;

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        if (blockUri == null || worldProvider == null) {
            return BehaviorState.FAILURE;
        }
        LocationComponent loc = actor.getComponent(LocationComponent.class);
        if (loc == null) {
            return BehaviorState.FAILURE;
        }

        org.joml.Vector3fc pos = loc.getLocalPosition();
        int r = (int) searchRadius;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    Vector3i checkPos = new Vector3i(
                            (int) pos.x() + x, (int) pos.y() + y, (int) pos.z() + z);
                    Block block = worldProvider.getBlock(checkPos);
                    if (block != null && block.getURI().toString().equals(blockUri)) {
                        actor.writeToBlackboard("foundResourcePosition",
                                new Vector3f(checkPos.x, checkPos.y, checkPos.z));
                        actor.writeToBlackboard("foundResourceBlockUri", blockUri);
                        return BehaviorState.SUCCESS;
                    }
                }
            }
        }
        return BehaviorState.FAILURE;
    }
}
