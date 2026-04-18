// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree.leaf;

import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorNode;
import org.terasology.module.behaviorai.tree.BehaviorStatus;

/**
 * Boolean test against the context. Always SUCCESS or FAILURE — never RUNNING.
 */
public abstract class ConditionNode implements BehaviorNode {
    @Override
    public final BehaviorStatus tick(BehaviorContext context) {
        return evaluate(context) ? BehaviorStatus.SUCCESS : BehaviorStatus.FAILURE;
    }

    protected abstract boolean evaluate(BehaviorContext context);
}
