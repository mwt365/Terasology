// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.integrationenvironment.jupiter.IntegrationEnvironment;
import org.terasology.engine.registry.In;
import org.terasology.module.advancednpcai.actions.resource.GatherResourceAction;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationEnvironment(dependencies = "AdvancedNpcAI")
public class GatherResourceActionTest {
    @In
    protected EntityManager entityManager;

    @Test
    public void testGatherReturnsRunningDuringTimer() {
        EntityRef entity = entityManager.create();
        Actor actor = new Actor(entity);
        actor.setDelta(0.5f);
        actor.writeToBlackboard("foundResourcePosition", new Vector3f(5, 5, 5));

        GatherResourceAction action = new GatherResourceAction();
        action.setId(1);
        action.construct(actor);

        BehaviorState state = action.modify(actor, BehaviorState.UNDEFINED);
        assertEquals(BehaviorState.RUNNING, state);
    }

    @Test
    public void testGatherIncrementsInventoryOnCompletion() {
        EntityRef entity = entityManager.create();
        Actor actor = new Actor(entity);
        actor.setDelta(10f);
        actor.writeToBlackboard("foundResourcePosition", new Vector3f(5, 5, 5));

        GatherResourceAction action = new GatherResourceAction();
        action.setId(1);
        action.construct(actor);

        BehaviorState state = action.modify(actor, BehaviorState.UNDEFINED);
        assertEquals(BehaviorState.SUCCESS, state);

        Integer count = actor.readFromBlackboard("inventoryResourceCount");
        assertEquals(1, count);
    }
}
