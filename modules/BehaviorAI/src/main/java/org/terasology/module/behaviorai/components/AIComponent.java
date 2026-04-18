// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.components;

import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Marker + configuration component placed on any entity that should be
 * driven by the BehaviorAI framework.
 *
 * The actual tree, goals, and memory are split into their own components so
 * they can be inspected / replicated independently (ECS-friendly) and so an
 * entity can swap goal sets without touching the tree runner.
 */
public class AIComponent implements Component<AIComponent> {

    /** Higher values tick less frequently (1 = every frame). */
    public int tickDivisor = 1;

    /** Enables the AI system for this entity. Set to false to pause. */
    public boolean enabled = true;

    /** Updated internally; counts ticks since last evaluation. */
    public transient int tickCounter;

    @Override
    public void copyFrom(AIComponent other) {
        this.tickDivisor = other.tickDivisor;
        this.enabled = other.enabled;
        this.tickCounter = other.tickCounter;
    }
}
