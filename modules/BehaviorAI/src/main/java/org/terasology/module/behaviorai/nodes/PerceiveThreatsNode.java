// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.nodes;

import org.terasology.module.behaviorai.components.MemoryComponent;
import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorStatus;
import org.terasology.module.behaviorai.tree.leaf.ActionNode;

import java.util.function.BiConsumer;

/**
 * Hook for a perception scan. The actual sensing (ray-casting, region query,
 * etc.) is injected as a {@link BiConsumer} so this module stays decoupled
 * from the world / physics. Always returns SUCCESS.
 */
public class PerceiveThreatsNode extends ActionNode {
    private final BiConsumer<Object, MemoryComponent> scan;

    public PerceiveThreatsNode(BiConsumer<Object, MemoryComponent> scan) {
        this.scan = scan;
    }

    @Override
    public BehaviorStatus tick(BehaviorContext context) {
        if (context.getMemory() != null) {
            scan.accept(context.getActor(), context.getMemory());
        }
        return BehaviorStatus.SUCCESS;
    }
}
