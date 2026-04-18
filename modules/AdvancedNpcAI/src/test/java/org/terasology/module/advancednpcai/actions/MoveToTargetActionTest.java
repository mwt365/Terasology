// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.integrationenvironment.jupiter.IntegrationEnvironment;
import org.terasology.engine.registry.In;
import org.terasology.module.advancednpcai.actions.movement.MoveToTargetAction;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationEnvironment(dependencies = "AdvancedNpcAI")
public class MoveToTargetActionTest {
    @In
    protected EntityManager entityManager;

    @Test
    public void testMoveToTargetReturnsRunning() {
        EntityRef entity = entityManager.create();
        entity.addComponent(new LocationComponent(new Vector3f(0, 0, 0)));

        Actor actor = new Actor(entity);
        actor.setDelta(0.1f);
        actor.writeToBlackboard("moveTarget", new Vector3f(100, 0, 0));

        MoveToTargetAction action = new MoveToTargetAction();
        action.setId(1);
        action.construct(actor);
        BehaviorState state = action.modify(actor, BehaviorState.UNDEFINED);

        assertEquals(BehaviorState.RUNNING, state);
    }

    @Test
    public void testMoveToTargetReturnsFailureWithNoTarget() {
        EntityRef entity = entityManager.create();
        entity.addComponent(new LocationComponent(new Vector3f(0, 0, 0)));

        Actor actor = new Actor(entity);
        actor.setDelta(0.1f);

        MoveToTargetAction action = new MoveToTargetAction();
        action.setId(1);
        BehaviorState state = action.modify(actor, BehaviorState.UNDEFINED);

        assertEquals(BehaviorState.FAILURE, state);
    }
}
