// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree;

import org.junit.jupiter.api.Test;
import org.terasology.module.behaviorai.tree.decorator.InverterNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InverterNodeTest {

    private static BehaviorContext ctx() {
        return new BehaviorContext(new Object(), null, 0.016f);
    }

    @Test
    void swapsSuccessAndFailure() {
        assertEquals(BehaviorStatus.FAILURE,
                new InverterNode(new StubNode(BehaviorStatus.SUCCESS)).tick(ctx()));
        assertEquals(BehaviorStatus.SUCCESS,
                new InverterNode(new StubNode(BehaviorStatus.FAILURE)).tick(ctx()));
    }

    @Test
    void passesRunningThrough() {
        assertEquals(BehaviorStatus.RUNNING,
                new InverterNode(new StubNode(BehaviorStatus.RUNNING)).tick(ctx()));
    }
}
