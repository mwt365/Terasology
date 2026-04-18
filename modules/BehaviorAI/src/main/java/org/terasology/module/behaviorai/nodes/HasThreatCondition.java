// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.nodes;

import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.leaf.ConditionNode;

public class HasThreatCondition extends ConditionNode {
    @Override
    protected boolean evaluate(BehaviorContext context) {
        return context.getMemory() != null && context.getMemory().hasThreat();
    }
}
