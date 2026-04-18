// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.goals;

import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorStatus;
import org.terasology.module.behaviorai.tree.leaf.ActionNode;

/** Do-nothing fallback. Always available, lowest priority. */
public class IdleGoal extends Goal {
    public IdleGoal() {
        super("idle", new ActionNode() {
            @Override
            public BehaviorStatus tick(BehaviorContext context) {
                return BehaviorStatus.SUCCESS;
            }
        });
    }

    @Override
    public float score(BehaviorContext context) {
        return GoalPriority.FILLER;
    }
}
