// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree;

import org.junit.jupiter.api.Test;
import org.terasology.module.behaviorai.tree.composite.SelectorNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectorNodeTest {

    private static BehaviorContext ctx() {
        return new BehaviorContext(new Object(), null, 0.016f);
    }

    @Test
    void firstSuccessShortCircuits() {
        StubNode a = new StubNode(BehaviorStatus.FAILURE);
        StubNode b = new StubNode(BehaviorStatus.SUCCESS);
        StubNode c = new StubNode(BehaviorStatus.FAILURE);
        SelectorNode sel = new SelectorNode(a, b, c);

        assertEquals(BehaviorStatus.SUCCESS, sel.tick(ctx()));
        assertEquals(1, a.tickCount);
        assertEquals(1, b.tickCount);
        assertEquals(0, c.tickCount);
    }

    @Test
    void runningHoldsCurrentChild() {
        StubNode a = new StubNode(BehaviorStatus.FAILURE);
        StubNode b = new StubNode(BehaviorStatus.RUNNING, BehaviorStatus.SUCCESS);
        StubNode c = new StubNode(BehaviorStatus.SUCCESS);
        SelectorNode sel = new SelectorNode(a, b, c);

        assertEquals(BehaviorStatus.RUNNING, sel.tick(ctx()));
        assertEquals(BehaviorStatus.SUCCESS, sel.tick(ctx()));
        assertEquals(1, a.tickCount, "a should not re-run after being skipped");
        assertEquals(2, b.tickCount);
        assertEquals(0, c.tickCount);
    }

    @Test
    void allFailuresFail() {
        SelectorNode sel = new SelectorNode(
                new StubNode(BehaviorStatus.FAILURE),
                new StubNode(BehaviorStatus.FAILURE));
        assertEquals(BehaviorStatus.FAILURE, sel.tick(ctx()));
    }
}
