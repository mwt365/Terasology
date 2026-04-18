// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree.composite;

import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorNode;
import org.terasology.module.behaviorai.tree.BehaviorStatus;

import java.util.List;

/**
 * Ticks all non-finished children each tick.
 *
 * Policy:
 *   REQUIRE_ONE  — first SUCCESS wins; returns SUCCESS. All FAILURE means FAILURE.
 *   REQUIRE_ALL  — any FAILURE wins; returns FAILURE. All SUCCESS means SUCCESS.
 */
public class ParallelNode extends CompositeNode {

    public enum Policy { REQUIRE_ONE, REQUIRE_ALL }

    private final Policy successPolicy;
    private final Policy failurePolicy;

    public ParallelNode(Policy successPolicy, Policy failurePolicy, List<BehaviorNode> children) {
        super(children);
        this.successPolicy = successPolicy;
        this.failurePolicy = failurePolicy;
    }

    public ParallelNode(Policy successPolicy, Policy failurePolicy, BehaviorNode... children) {
        super(children);
        this.successPolicy = successPolicy;
        this.failurePolicy = failurePolicy;
    }

    @Override
    public BehaviorStatus tick(BehaviorContext context) {
        int successes = 0;
        int failures = 0;
        for (BehaviorNode child : children) {
            BehaviorStatus result = child.tick(context);
            if (result == BehaviorStatus.SUCCESS) {
                successes++;
            } else if (result == BehaviorStatus.FAILURE) {
                failures++;
            }
        }
        if (successPolicy == Policy.REQUIRE_ONE && successes >= 1) {
            reset();
            return BehaviorStatus.SUCCESS;
        }
        if (successPolicy == Policy.REQUIRE_ALL && successes == children.size()) {
            reset();
            return BehaviorStatus.SUCCESS;
        }
        if (failurePolicy == Policy.REQUIRE_ONE && failures >= 1) {
            reset();
            return BehaviorStatus.FAILURE;
        }
        if (failurePolicy == Policy.REQUIRE_ALL && failures == children.size()) {
            reset();
            return BehaviorStatus.FAILURE;
        }
        return BehaviorStatus.RUNNING;
    }
}
