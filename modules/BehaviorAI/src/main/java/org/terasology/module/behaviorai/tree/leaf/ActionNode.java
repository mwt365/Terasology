// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree.leaf;

import org.terasology.module.behaviorai.tree.BehaviorNode;

/**
 * Leaf base class for effectful work. Subclasses implement {@code tick()}
 * returning SUCCESS, FAILURE, or RUNNING depending on whether their action
 * completed this tick, failed, or needs more ticks.
 *
 * Override {@link #reset()} to clear any per-invocation state when the node
 * is preempted.
 */
public abstract class ActionNode implements BehaviorNode {
}
