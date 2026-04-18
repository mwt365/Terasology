// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.nodes;

/**
 * Pluggable movement back-end. The framework does not depend on any particular
 * pathfinder or physics integration — callers supply one (or a test stub).
 *
 * {@link #step(Object, float, float, float, float)} should advance the actor
 * toward the target over {@code delta} seconds and return true when arrived.
 */
public interface Navigator {
    boolean step(Object actor, float targetX, float targetY, float targetZ, float delta);
}
