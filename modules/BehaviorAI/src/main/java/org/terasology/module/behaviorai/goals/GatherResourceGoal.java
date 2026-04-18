// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.goals;

import org.terasology.module.behaviorai.components.MemoryComponent;
import org.terasology.module.behaviorai.nodes.HarvestResourceNode;
import org.terasology.module.behaviorai.nodes.HasResourceCondition;
import org.terasology.module.behaviorai.nodes.MoveToNode;
import org.terasology.module.behaviorai.nodes.Navigator;
import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.composite.SequenceNode;

/**
 * Multi-step plan: {@code HasResource -> MoveToResource -> Harvest}. If any
 * step fails the goal reports FAILURE for this tick and the arbiter may
 * pick a different goal next tick.
 */
public class GatherResourceGoal extends Goal {

    public GatherResourceGoal(Navigator navigator) {
        super("gather", new SequenceNode(
                new HasResourceCondition(),
                new MoveToNode(navigator, memory -> memory.resources.isEmpty() ? null : memory.resources.get(0)),
                new HarvestResourceNode()));
    }

    @Override
    public float score(BehaviorContext context) {
        MemoryComponent memory = context.getMemory();
        if (memory == null || memory.resources.isEmpty()) {
            return 0f;
        }
        return GoalPriority.PRODUCTIVE;
    }
}
