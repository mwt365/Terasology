// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.goals;

import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorNode;

/**
 * A goal is a named behavior tree plus a utility function. The AI system
 * picks the goal with the highest utility each tick and ticks its tree.
 *
 * Subclasses build their tree once (in the constructor) and expose a cheap
 * {@link #score(BehaviorContext)} that can be called every tick. Trees should
 * tolerate being reset mid-flight — the system calls {@link BehaviorNode#reset()}
 * when switching goals.
 */
public abstract class Goal {
    private final String name;
    private final BehaviorNode root;

    protected Goal(String name, BehaviorNode root) {
        this.name = name;
        this.root = root;
    }

    public String getName() {
        return name;
    }

    public BehaviorNode getRoot() {
        return root;
    }

    /** Higher means "I should run now." Returns 0 when irrelevant. */
    public abstract float score(BehaviorContext context);
}
