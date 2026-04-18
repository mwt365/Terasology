// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.systems;

import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.registry.In;
import org.terasology.module.behaviorai.components.AIComponent;
import org.terasology.module.behaviorai.components.GoalComponent;
import org.terasology.module.behaviorai.components.MemoryComponent;
import org.terasology.module.behaviorai.events.AIGoalChangedEvent;

/**
 * Drives every entity with {@link AIComponent} and {@link GoalComponent}
 * each frame. Scoring, selection, and tree execution are delegated to
 * {@link AIArbiter}; this class is thin ECS glue so the arbiter can be
 * exercised in unit tests without a running engine.
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class AIUpdateSystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    @In
    private EntityManager entityManager;

    @Override
    public void update(float delta) {
        for (EntityRef entity : entityManager.getEntitiesWith(AIComponent.class, GoalComponent.class)) {
            tickEntity(entity, delta);
        }
    }

    public void tickEntity(EntityRef entity, float delta) {
        AIComponent ai = entity.getComponent(AIComponent.class);
        if (ai == null || !ai.enabled) {
            return;
        }
        ai.tickCounter++;
        if (ai.tickDivisor > 1 && (ai.tickCounter % ai.tickDivisor) != 0) {
            return;
        }

        GoalComponent goals = entity.getComponent(GoalComponent.class);
        MemoryComponent memory = entity.getComponent(MemoryComponent.class);
        AIArbiter.tick(entity, goals, memory, delta,
                (prev, next) -> entity.send(new AIGoalChangedEvent(prev, next)));
    }
}
