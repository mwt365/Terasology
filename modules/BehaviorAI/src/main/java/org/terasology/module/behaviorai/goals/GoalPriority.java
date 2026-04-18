// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.goals;

/**
 * Baseline utility values. Goals add/subtract from these to make nuanced
 * decisions, but the ordering here is the default safety > survival >
 * productive > filler pyramid the framework ships.
 */
public final class GoalPriority {
    public static final float SAFETY = 100f;
    public static final float SURVIVAL = 60f;
    public static final float PRODUCTIVE = 30f;
    public static final float FILLER = 1f;

    private GoalPriority() {
    }
}
