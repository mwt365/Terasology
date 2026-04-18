// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree;

import org.junit.jupiter.api.Test;
import org.terasology.module.behaviorai.tree.decorator.RepeatNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepeatNodeTest {

    private static BehaviorContext ctx() {
        return new BehaviorContext(new Object(), null, 0.016f);
    }

    @Test
    void succeedsAfterCountIterations() {
        StubNode child = new StubNode();
        child.fallback = BehaviorStatus.SUCCESS;
        RepeatNode repeat = new RepeatNode(3, child);

        assertEquals(BehaviorStatus.RUNNING, repeat.tick(ctx()));
        assertEquals(BehaviorStatus.RUNNING, repeat.tick(ctx()));
        assertEquals(BehaviorStatus.SUCCESS, repeat.tick(ctx()));
        assertEquals(3, child.tickCount);
    }

    @Test
    void failurePropagates() {
        RepeatNode repeat = new RepeatNode(5, new StubNode(BehaviorStatus.FAILURE));
        assertEquals(BehaviorStatus.FAILURE, repeat.tick(ctx()));
    }
}
