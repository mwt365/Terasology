// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree;

import org.junit.jupiter.api.Test;
import org.terasology.module.behaviorai.tree.composite.ParallelNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParallelNodeTest {

    private static BehaviorContext ctx() {
        return new BehaviorContext(new Object(), null, 0.016f);
    }

    @Test
    void requireOneSuccessReturnsSuccess() {
        ParallelNode p = new ParallelNode(
                ParallelNode.Policy.REQUIRE_ONE, ParallelNode.Policy.REQUIRE_ALL,
                new StubNode(BehaviorStatus.RUNNING),
                new StubNode(BehaviorStatus.SUCCESS));
        assertEquals(BehaviorStatus.SUCCESS, p.tick(ctx()));
    }

    @Test
    void requireAllFailureWhenAnyFails() {
        ParallelNode p = new ParallelNode(
                ParallelNode.Policy.REQUIRE_ALL, ParallelNode.Policy.REQUIRE_ONE,
                new StubNode(BehaviorStatus.SUCCESS),
                new StubNode(BehaviorStatus.FAILURE));
        assertEquals(BehaviorStatus.FAILURE, p.tick(ctx()));
    }

    @Test
    void allRunningYieldsRunning() {
        ParallelNode p = new ParallelNode(
                ParallelNode.Policy.REQUIRE_ALL, ParallelNode.Policy.REQUIRE_ONE,
                new StubNode(BehaviorStatus.RUNNING),
                new StubNode(BehaviorStatus.RUNNING));
        assertEquals(BehaviorStatus.RUNNING, p.tick(ctx()));
    }
}
