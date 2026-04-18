// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.module.advancednpcai.components.AIComponent;
import org.terasology.module.advancednpcai.components.GoalComponent;
import org.terasology.module.advancednpcai.events.GoalChangedEvent;
import org.terasology.module.advancednpcai.events.GoalCompletedEvent;
import org.terasology.module.advancednpcai.model.Goal;
import org.terasology.module.advancednpcai.model.GoalType;

@RegisterSystem(RegisterMode.AUTHORITY)
@Share(GoalSystem.class)
public class GoalSystem extends BaseComponentSystem {
    private static final Logger logger = LoggerFactory.getLogger(GoalSystem.class);

    @In
    private EntityManager entityManager;
    @In
    private Time time;

    public void addGoal(EntityRef entity, Goal goal) {
        GoalComponent gc = entity.getComponent(GoalComponent.class);
        if (gc == null) {
            return;
        }

        goal.timeStarted = time.getGameTime();

        while (gc.goals.size() >= gc.maxGoals) {
            Goal toRemove = null;
            for (int i = gc.goals.size() - 1; i >= 0; i--) {
                if (gc.goals.get(i).completed) {
                    toRemove = gc.goals.get(i);
                    break;
                }
            }
            if (toRemove == null && !gc.goals.isEmpty()) {
                toRemove = gc.goals.get(gc.goals.size() - 1);
            }
            if (toRemove != null) {
                gc.goals.remove(toRemove);
            } else {
                break;
            }
        }

        Goal previousActive = gc.getActiveGoal();
        gc.goals.add(goal);
        gc.goals.sort((a, b) -> Integer.compare(b.priority, a.priority));
        entity.saveComponent(gc);

        Goal newActive = gc.getActiveGoal();
        if (previousActive != newActive) {
            entity.send(new GoalChangedEvent(previousActive, newActive));
        }
    }

    public void completeActiveGoal(EntityRef entity) {
        GoalComponent gc = entity.getComponent(GoalComponent.class);
        if (gc == null) {
            return;
        }
        Goal active = gc.getActiveGoal();
        if (active == null) {
            return;
        }

        active.completed = true;
        entity.saveComponent(gc);
        entity.send(new GoalCompletedEvent(active));

        Goal newActive = gc.getActiveGoal();
        if (newActive != null) {
            entity.send(new GoalChangedEvent(active, newActive));
        }
    }

    public void pruneCompletedGoals(EntityRef entity) {
        GoalComponent gc = entity.getComponent(GoalComponent.class);
        if (gc == null) {
            return;
        }
        gc.goals.removeIf(g -> g.completed);
        entity.saveComponent(gc);
    }

    @ReceiveEvent(components = AIComponent.class)
    public void onGoalCompleted(GoalCompletedEvent event, EntityRef entity) {
        GoalComponent gc = entity.getComponent(GoalComponent.class);
        if (gc == null) {
            return;
        }
        if (gc.getActiveGoal() == null) {
            addGoal(entity, new Goal(GoalType.IDLE, 0));
        }
    }
}
