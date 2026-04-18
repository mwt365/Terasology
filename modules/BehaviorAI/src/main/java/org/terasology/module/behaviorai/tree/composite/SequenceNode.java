// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree.composite;

import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorNode;
import org.terasology.module.behaviorai.tree.BehaviorStatus;

import java.util.List;

/**
 * Ticks children in order until one returns {@code FAILURE} or {@code RUNNING}.
 * Returns {@code SUCCESS} only if every child succeeds.
 */
public class SequenceNode extends CompositeNode {
    public SequenceNode(List<BehaviorNode> children) {
        super(children);
    }

    public SequenceNode(BehaviorNode... children) {
        super(children);
    }

    @Override
    public BehaviorStatus tick(BehaviorContext context) {
        while (currentIndex < children.size()) {
            BehaviorStatus result = children.get(currentIndex).tick(context);
            if (result == BehaviorStatus.FAILURE) {
                reset();
                return BehaviorStatus.FAILURE;
            }
            if (result == BehaviorStatus.RUNNING) {
                return BehaviorStatus.RUNNING;
            }
            currentIndex++;
        }
        reset();
        return BehaviorStatus.SUCCESS;
    }
}
