// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree.composite;

import org.terasology.module.behaviorai.tree.BehaviorNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class CompositeNode implements BehaviorNode {
    protected final List<BehaviorNode> children;
    protected int currentIndex;

    protected CompositeNode(List<BehaviorNode> children) {
        this.children = new ArrayList<>(children);
    }

    protected CompositeNode(BehaviorNode... children) {
        this(Arrays.asList(children));
    }

    public List<BehaviorNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public CompositeNode add(BehaviorNode child) {
        children.add(child);
        return this;
    }

    @Override
    public void reset() {
        currentIndex = 0;
        for (BehaviorNode child : children) {
            child.reset();
        }
    }
}
