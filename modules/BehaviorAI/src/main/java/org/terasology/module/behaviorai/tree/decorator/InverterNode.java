// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree.decorator;

import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorNode;
import org.terasology.module.behaviorai.tree.BehaviorStatus;

/** Swaps SUCCESS and FAILURE of its child; RUNNING is passed through. */
public class InverterNode extends DecoratorNode {
    public InverterNode(BehaviorNode child) {
        super(child);
    }

    @Override
    public BehaviorStatus tick(BehaviorContext context) {
        BehaviorStatus result = child.tick(context);
        switch (result) {
            case SUCCESS: return BehaviorStatus.FAILURE;
            case FAILURE: return BehaviorStatus.SUCCESS;
            default:      return BehaviorStatus.RUNNING;
        }
    }
}
