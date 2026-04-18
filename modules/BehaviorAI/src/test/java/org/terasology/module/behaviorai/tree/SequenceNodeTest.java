// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree;

import org.junit.jupiter.api.Test;
import org.terasology.module.behaviorai.tree.composite.SequenceNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequenceNodeTest {

    private static BehaviorContext ctx() {
        return new BehaviorContext(new Object(), null, 0.016f);
    }

    @Test
    void succeedsOnlyWhenAllChildrenSucceed() {
        SequenceNode seq = new SequenceNode(
                new StubNode(BehaviorStatus.SUCCESS),
                new StubNode(BehaviorStatus.SUCCESS),
                new StubNode(BehaviorStatus.SUCCESS));
        assertEquals(BehaviorStatus.SUCCESS, seq.tick(ctx()));
    }

    @Test
    void firstFailureShortCircuits() {
        StubNode a = new StubNode(BehaviorStatus.SUCCESS);
        StubNode b = new StubNode(BehaviorStatus.FAILURE);
        StubNode c = new StubNode(BehaviorStatus.SUCCESS);
        SequenceNode seq = new SequenceNode(a, b, c);

        assertEquals(BehaviorStatus.FAILURE, seq.tick(ctx()));
        assertEquals(1, a.tickCount);
        assertEquals(1, b.tickCount);
        assertEquals(0, c.tickCount);
    }

    @Test
    void runningResumesOnNextTick() {
        StubNode a = new StubNode(BehaviorStatus.SUCCESS);
        StubNode b = new StubNode(BehaviorStatus.RUNNING, BehaviorStatus.SUCCESS);
        StubNode c = new StubNode(BehaviorStatus.SUCCESS);
        SequenceNode seq = new SequenceNode(a, b, c);

        assertEquals(BehaviorStatus.RUNNING, seq.tick(ctx()));
        assertEquals(BehaviorStatus.SUCCESS, seq.tick(ctx()));
        assertEquals(1, a.tickCount);
        assertEquals(2, b.tickCount);
        assertEquals(1, c.tickCount);
    }
}
