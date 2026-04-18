// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.nodes;

import org.terasology.module.behaviorai.components.MemoryComponent;
import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorStatus;
import org.terasology.module.behaviorai.tree.leaf.ActionNode;

import java.util.function.Function;

/**
 * Walks the actor toward a percept chosen from memory. RUNNING while in
 * transit, SUCCESS on arrival, FAILURE when the target provider returns
 * null.
 *
 * The target provider is injected so the same node can be used for
 * "walk to nearest resource", "walk to safe spot", etc.
 */
public class MoveToNode extends ActionNode {
    private final Navigator navigator;
    private final Function<MemoryComponent, MemoryComponent.Percept> target;

    public MoveToNode(Navigator navigator,
                      Function<MemoryComponent, MemoryComponent.Percept> target) {
        this.navigator = navigator;
        this.target = target;
    }

    @Override
    public BehaviorStatus tick(BehaviorContext context) {
        MemoryComponent memory = context.getMemory();
        if (memory == null) {
            return BehaviorStatus.FAILURE;
        }
        MemoryComponent.Percept dest = target.apply(memory);
        if (dest == null) {
            return BehaviorStatus.FAILURE;
        }
        boolean arrived = navigator.step(context.getActor(), dest.x, dest.y, dest.z, context.getDelta());
        return arrived ? BehaviorStatus.SUCCESS : BehaviorStatus.RUNNING;
    }
}
