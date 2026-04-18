// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.systems;

import org.terasology.module.behaviorai.components.GoalComponent;
import org.terasology.module.behaviorai.components.MemoryComponent;
import org.terasology.module.behaviorai.goals.Goal;
import org.terasology.module.behaviorai.tree.BehaviorContext;
import org.terasology.module.behaviorai.tree.BehaviorStatus;

import java.util.function.BiConsumer;

/**
 * Pure-logic counterpart of {@link AIUpdateSystem}. Contains zero engine
 * types so it can be unit-tested directly without a running ECS harness.
 *
 * The system glue layer delegates here; tests exercise this class against
 * in-memory {@link GoalComponent} / {@link MemoryComponent} instances.
 */
public final class AIArbiter {

    /** Callback signature: (previousGoalName, newGoalName). */
    @FunctionalInterface
    public interface GoalChangeListener extends BiConsumer<String, String> {
    }

    private AIArbiter() {
    }

    /**
     * Pick the highest-scoring goal, handle switch bookkeeping, and tick
     * the winner's tree. Returns the status the winning tree reported
     * this tick, or {@code FAILURE} if no goal was eligible.
     */
    public static BehaviorStatus tick(Object actor,
                                      GoalComponent goals,
                                      MemoryComponent memory,
                                      float delta,
                                      GoalChangeListener onChange) {
        if (goals == null || goals.goals.isEmpty()) {
            return BehaviorStatus.FAILURE;
        }
        BehaviorContext context = new BehaviorContext(actor, memory, delta);
        Goal best = pick(goals, context);
        if (best == null) {
            return BehaviorStatus.FAILURE;
        }
        if (goals.activeGoal != best) {
            if (goals.activeGoal != null) {
                goals.activeGoal.getRoot().reset();
            }
            String prev = goals.activeGoal == null ? null : goals.activeGoal.getName();
            goals.activeGoal = best;
            if (onChange != null) {
                onChange.accept(prev, best.getName());
            }
        }
        return best.getRoot().tick(context);
    }

    private static Goal pick(GoalComponent goals, BehaviorContext context) {
        Goal best = null;
        float bestScore = Float.NEGATIVE_INFINITY;
        for (Goal goal : goals.goals) {
            float score = goal.score(context);
            if (score > bestScore) {
                bestScore = score;
                best = goal;
            }
        }
        return best;
    }
}
