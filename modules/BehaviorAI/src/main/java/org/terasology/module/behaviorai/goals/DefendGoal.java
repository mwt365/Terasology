// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.goals;

import org.terasology.module.behaviorai.components.MemoryComponent;
import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorStatus;
import org.terasology.module.behaviorai.tree.leaf.ActionNode;

import java.util.function.Predicate;

/**
 * Alternative safety response: stand ground when the caller's predicate
 * says defense is preferable (e.g. full health, armed). The engage action
 * is injected to keep the module decoupled from combat systems.
 */
public class DefendGoal extends Goal {
    private final Predicate<BehaviorContext> shouldDefend;

    public DefendGoal(Predicate<BehaviorContext> shouldDefend, ActionNode engage) {
        super("defend", engage);
        this.shouldDefend = shouldDefend;
    }

    public DefendGoal(Predicate<BehaviorContext> shouldDefend) {
        this(shouldDefend, new ActionNode() {
            @Override
            public BehaviorStatus tick(BehaviorContext context) {
                if (context.getMemory() != null) {
                    context.getMemory().forgetThreats();
                }
                return BehaviorStatus.SUCCESS;
            }
        });
    }

    @Override
    public float score(BehaviorContext context) {
        MemoryComponent memory = context.getMemory();
        if (memory == null || !memory.hasThreat() || !shouldDefend.test(context)) {
            return 0f;
        }
        return GoalPriority.SAFETY + 0.5f;
    }
}
