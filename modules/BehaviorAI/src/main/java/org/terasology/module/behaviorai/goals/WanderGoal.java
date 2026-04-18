// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.goals;

import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorStatus;
import org.terasology.module.behaviorai.tree.leaf.ActionNode;

/**
 * Low-priority default motion. The actual wander step is delegated to an
 * injected action so this module does not assume a physics/movement model.
 */
public class WanderGoal extends Goal {
    public WanderGoal(ActionNode wanderStep) {
        super("wander", wanderStep);
    }

    public WanderGoal() {
        this(new ActionNode() {
            @Override
            public BehaviorStatus tick(BehaviorContext context) {
                return BehaviorStatus.SUCCESS;
            }
        });
    }

    @Override
    public float score(BehaviorContext context) {
        return GoalPriority.FILLER + 1f;
    }
}
