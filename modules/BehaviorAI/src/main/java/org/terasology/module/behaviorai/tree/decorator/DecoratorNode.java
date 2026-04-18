// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree.decorator;

import org.terasology.module.behaviorai.tree.BehaviorNode;

public abstract class DecoratorNode implements BehaviorNode {
    protected final BehaviorNode child;

    protected DecoratorNode(BehaviorNode child) {
        this.child = child;
    }

    public BehaviorNode getChild() {
        return child;
    }

    @Override
    public void reset() {
        child.reset();
    }
}
