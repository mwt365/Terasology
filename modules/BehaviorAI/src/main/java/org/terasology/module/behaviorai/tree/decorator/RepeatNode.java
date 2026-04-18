// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree.decorator;

import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorNode;
import org.terasology.module.behaviorai.tree.BehaviorStatus;

/**
 * Repeats its child up to {@code count} times. A negative count repeats forever
 * (the node returns RUNNING each tick a completed child finishes).
 *
 * Stops early if the child returns FAILURE.
 */
public class RepeatNode extends DecoratorNode {
    private final int count;
    private int completed;

    public RepeatNode(int count, BehaviorNode child) {
        super(child);
        this.count = count;
    }

    @Override
    public BehaviorStatus tick(BehaviorContext context) {
        BehaviorStatus result = child.tick(context);
        if (result == BehaviorStatus.RUNNING) {
            return BehaviorStatus.RUNNING;
        }
        if (result == BehaviorStatus.FAILURE) {
            reset();
            return BehaviorStatus.FAILURE;
        }
        completed++;
        child.reset();
        if (count >= 0 && completed >= count) {
            reset();
            return BehaviorStatus.SUCCESS;
        }
        return BehaviorStatus.RUNNING;
    }

    @Override
    public void reset() {
        super.reset();
        completed = 0;
    }
}
