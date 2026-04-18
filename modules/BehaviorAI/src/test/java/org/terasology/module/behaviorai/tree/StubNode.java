// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree;

import java.util.ArrayDeque;
import java.util.Deque;

/** Scripted node — returns a queued sequence of statuses. Used in BT tests. */
public class StubNode implements BehaviorNode {
    public final Deque<BehaviorStatus> script = new ArrayDeque<>();
    public int tickCount;
    public int resetCount;
    public BehaviorStatus fallback = BehaviorStatus.SUCCESS;

    public StubNode(BehaviorStatus... statuses) {
        for (BehaviorStatus s : statuses) {
            script.add(s);
        }
    }

    @Override
    public BehaviorStatus tick(BehaviorContext context) {
        tickCount++;
        return script.isEmpty() ? fallback : script.removeFirst();
    }

    @Override
    public void reset() {
        resetCount++;
    }
}
