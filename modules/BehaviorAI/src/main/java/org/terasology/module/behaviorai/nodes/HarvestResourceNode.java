// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.nodes;

import org.terasology.module.behaviorai.components.MemoryComponent;
import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorStatus;
import org.terasology.module.behaviorai.tree.leaf.ActionNode;

/**
 * Consumes the nearest known resource from memory, accumulating a counter
 * on the blackboard under the key {@value #HARVESTED_KEY}. Returns FAILURE
 * if there is nothing to harvest.
 *
 * Game integrations should subclass and override {@link #collect} to apply
 * real world effects (spawn items, destroy block, etc.) while preserving
 * the memory bookkeeping this class handles.
 */
public class HarvestResourceNode extends ActionNode {
    public static final String HARVESTED_KEY = "harvested";

    @Override
    public BehaviorStatus tick(BehaviorContext context) {
        MemoryComponent memory = context.getMemory();
        if (memory == null || memory.resources.isEmpty()) {
            return BehaviorStatus.FAILURE;
        }
        MemoryComponent.Percept p = memory.resources.remove(0);
        collect(context, p);
        int count = (int) memory.blackboard.getOrDefault(HARVESTED_KEY, 0);
        memory.blackboard.put(HARVESTED_KEY, count + 1);
        return BehaviorStatus.SUCCESS;
    }

    protected void collect(BehaviorContext context, MemoryComponent.Percept percept) {
    }
}
