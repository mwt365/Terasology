// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.goals;

import org.junit.jupiter.api.Test;
import org.terasology.module.behaviorai.components.GoalComponent;
import org.terasology.module.behaviorai.components.MemoryComponent;
import org.terasology.module.behaviorai.nodes.HarvestResourceNode;
import org.terasology.module.behaviorai.nodes.Navigator;
import org.terasology.module.behaviorai.systems.AIArbiter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifies the interruption → resume flow: NPC starts gathering, a threat
 * appears mid-task, safety goal preempts, after the threat clears the NPC
 * returns to gathering and eventually harvests.
 *
 * Uses a slow navigator that takes two steps to arrive at a target so we
 * can interrupt the MoveTo step while it is RUNNING.
 */
class InterruptionTest {

    private static final class SlowNav implements Navigator {
        int steps;
        @Override
        public boolean step(Object actor, float x, float y, float z, float delta) {
            steps++;
            return steps % 2 == 0; // arrives on every second call
        }
    }

    @Test
    void threatInterruptsGatherAndNpcResumesAfterSafe() {
        SlowNav nav = new SlowNav();
        GoalComponent goals = new GoalComponent();
        goals.addGoal(new IdleGoal());
        goals.addGoal(new GatherResourceGoal(nav));
        goals.addGoal(new FleeGoal(nav));

        MemoryComponent memory = new MemoryComponent();
        memory.rememberResource(new MemoryComponent.Percept("tree", 10, 0, 0, 0f));

        List<String> transitions = new ArrayList<>();
        AIArbiter.GoalChangeListener listener = (prev, next) -> transitions.add(prev + "->" + next);

        // Tick 1: gather wins, MoveTo returns RUNNING (SlowNav step 1).
        AIArbiter.tick(new Object(), goals, memory, 0.1f, listener);
        assertEquals("gather", goals.activeGoal.getName());

        // Threat appears mid-gather.
        memory.rememberThreat(new MemoryComponent.Percept("wolf", 2, 0, 2, 0.1f));

        // Tick 2: flee preempts; MoveTo fails because safeSpot is null, so
        // the sequence reports FAILURE and threats are not yet cleared.
        AIArbiter.tick(new Object(), goals, memory, 0.1f, listener);
        assertEquals("flee", goals.activeGoal.getName());

        // Provide a safe spot. Tick 3: MoveTo arrives (SlowNav step 2),
        // ClearThreats fires, threats list empties.
        memory.safeSpot = new MemoryComponent.Percept("home", 0, 0, 0, 0.2f);
        AIArbiter.tick(new Object(), goals, memory, 0.1f, listener);

        // Tick 4: no threats, gather preempts flee, MoveTo RUNNING (step 3).
        // Tick 5: MoveTo arrives (step 4), Harvest runs.
        AIArbiter.tick(new Object(), goals, memory, 0.1f, listener);
        AIArbiter.tick(new Object(), goals, memory, 0.1f, listener);
        assertEquals("gather", goals.activeGoal.getName());
        assertEquals(1, memory.blackboard.get(HarvestResourceNode.HARVESTED_KEY),
                "gather should have completed once the threat was resolved");

        // At minimum we saw an entry into gather, a switch to flee, and a
        // switch back to gather.
        long switches = transitions.stream()
                .filter(t -> t.equals("gather->flee") || t.equals("flee->gather"))
                .count();
        assertEquals(2, switches, "expected one preemption and one resume: " + transitions);
    }
}
