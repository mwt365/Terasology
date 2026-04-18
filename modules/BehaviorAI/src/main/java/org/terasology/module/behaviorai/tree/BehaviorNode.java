// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree;

/**
 * Unit of work in a behavior tree. Nodes are stateless w.r.t. the tree structure
 * but may hold per-tick transient state; callers must invoke {@link #reset()}
 * when execution is preempted so the next tick starts cleanly.
 */
public interface BehaviorNode {
    BehaviorStatus tick(BehaviorContext context);

    /** Called when execution is abandoned mid-flight (e.g. goal switch). */
    default void reset() {
    }
}
