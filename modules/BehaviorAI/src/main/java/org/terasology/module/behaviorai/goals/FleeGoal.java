// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.goals;

import org.terasology.module.behaviorai.components.MemoryComponent;
import org.terasology.module.behaviorai.nodes.HasThreatCondition;
import org.terasology.module.behaviorai.nodes.MoveToNode;
import org.terasology.module.behaviorai.nodes.Navigator;
import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.composite.SequenceNode;
import org.terasology.module.behaviorai.tree.leaf.ActionNode;

/**
 * Safety-tier goal. Only scores when threats are in memory. The "flee"
 * action moves to {@link MemoryComponent#safeSpot}; if none is set, the
 * goal still succeeds in one tick (clearing threats) so the NPC does not
 * deadlock waiting for a safe point.
 */
public class FleeGoal extends Goal {

    public FleeGoal(Navigator navigator) {
        super("flee", new SequenceNode(
                new HasThreatCondition(),
                new MoveToNode(navigator, memory -> memory.safeSpot),
                new ClearThreatsAction()));
    }

    @Override
    public float score(BehaviorContext context) {
        MemoryComponent memory = context.getMemory();
        if (memory == null || !memory.hasThreat()) {
            return 0f;
        }
        return GoalPriority.SAFETY;
    }

    private static final class ClearThreatsAction extends ActionNode {
        @Override
        public org.terasology.module.behaviorai.tree.BehaviorStatus tick(BehaviorContext context) {
            if (context.getMemory() != null) {
                context.getMemory().forgetThreats();
            }
            return org.terasology.module.behaviorai.tree.BehaviorStatus.SUCCESS;
        }
    }
}
