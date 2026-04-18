// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree.composite;

import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorNode;
import org.terasology.module.behaviorai.tree.BehaviorStatus;

import java.util.List;

/**
 * Ticks children in order until one returns {@code SUCCESS} or {@code RUNNING}.
 * Returns {@code FAILURE} only if every child fails.
 */
public class SelectorNode extends CompositeNode {
    public SelectorNode(List<BehaviorNode> children) {
        super(children);
    }

    public SelectorNode(BehaviorNode... children) {
        super(children);
    }

    @Override
    public BehaviorStatus tick(BehaviorContext context) {
        while (currentIndex < children.size()) {
            BehaviorStatus result = children.get(currentIndex).tick(context);
            if (result == BehaviorStatus.SUCCESS) {
                reset();
                return BehaviorStatus.SUCCESS;
            }
            if (result == BehaviorStatus.RUNNING) {
                return BehaviorStatus.RUNNING;
            }
            currentIndex++;
        }
        reset();
        return BehaviorStatus.FAILURE;
    }
}
