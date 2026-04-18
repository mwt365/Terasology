// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.resource;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.WorldProvider;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;

@BehaviorAction(name = "gather_resource")
public class GatherResourceAction extends BaseAction {
    private float gatherTime = 2.0f;

    @In
    private transient WorldProvider worldProvider;
    @In
    private transient BlockManager blockManager;

    @Override
    public void construct(Actor actor) {
        actor.setValue(getId(), gatherTime);
    }

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        Float remaining = actor.getValue(getId());
        if (remaining == null) {
            return BehaviorState.FAILURE;
        }
        remaining -= actor.getDelta();
        actor.setValue(getId(), remaining);

        if (remaining > 0) {
            return BehaviorState.RUNNING;
        }

        Vector3f resourcePos = actor.readFromBlackboard("foundResourcePosition");
        if (resourcePos != null && worldProvider != null && blockManager != null) {
            Vector3i blockPos = new Vector3i(
                    (int) resourcePos.x, (int) resourcePos.y, (int) resourcePos.z);
            Block air = blockManager.getBlock(BlockManager.AIR_ID);
            worldProvider.setBlock(blockPos, air);
        }

        Integer count = actor.readFromBlackboard("inventoryResourceCount");
        actor.writeToBlackboard("inventoryResourceCount", (count == null ? 0 : count) + 1);
        return BehaviorState.SUCCESS;
    }
}
