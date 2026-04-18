// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree;

import org.terasology.module.behaviorai.components.MemoryComponent;

/**
 * Per-tick execution context handed to every node. Decouples nodes from the
 * ECS so the runtime is unit-testable without a running engine.
 *
 * The {@code actor} is an opaque handle (typically an {@code EntityRef}) so
 * this module does not hard-depend on the engine entity system.
 */
public final class BehaviorContext {
    private final Object actor;
    private final MemoryComponent memory;
    private final float delta;

    public BehaviorContext(Object actor, MemoryComponent memory, float delta) {
        this.actor = actor;
        this.memory = memory;
        this.delta = delta;
    }

    public Object getActor() {
        return actor;
    }

    public MemoryComponent getMemory() {
        return memory;
    }

    public float getDelta() {
        return delta;
    }
}
