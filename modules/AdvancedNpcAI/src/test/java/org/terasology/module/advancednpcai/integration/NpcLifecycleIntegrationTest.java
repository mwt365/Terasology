// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.integration;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.integrationenvironment.jupiter.IntegrationEnvironment;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.module.advancednpcai.components.AIComponent;
import org.terasology.module.advancednpcai.components.GoalComponent;
import org.terasology.module.advancednpcai.components.MemoryComponent;
import org.terasology.module.advancednpcai.components.NpcTypeComponent;
import org.terasology.module.advancednpcai.model.Goal;
import org.terasology.module.advancednpcai.model.GoalType;
import org.terasology.module.advancednpcai.model.ThreatLevel;
import org.terasology.module.advancednpcai.systems.GoalSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationEnvironment(dependencies = "AdvancedNpcAI")
public class NpcLifecycleIntegrationTest {
    @In
    protected EntityManager entityManager;
    @In
    protected GoalSystem goalSystem;

    @Test
    public void testNpcCreationWithAllComponents() {
        EntityRef npc = entityManager.create();
        npc.addComponent(new LocationComponent(new Vector3f(0, 0, 0)));
        npc.addComponent(new AIComponent());
        npc.addComponent(new NpcTypeComponent());

        assertNotNull(npc.getComponent(AIComponent.class));
        assertNotNull(npc.getComponent(GoalComponent.class));
        assertNotNull(npc.getComponent(MemoryComponent.class));
        assertNotNull(npc.getComponent(NpcTypeComponent.class));
    }

    @Test
    public void testNpcDefaultState() {
        EntityRef npc = entityManager.create();
        npc.addComponent(new LocationComponent(new Vector3f(0, 0, 0)));
        npc.addComponent(new AIComponent());

        AIComponent ai = npc.getComponent(AIComponent.class);
        assertTrue(ai.active);
        assertEquals(ThreatLevel.NONE, ai.currentThreatLevel);
    }

    @Test
    public void testGoalSystemIntegration() {
        EntityRef npc = entityManager.create();
        npc.addComponent(new LocationComponent(new Vector3f(0, 0, 0)));
        npc.addComponent(new AIComponent());

        Goal gatherGoal = new Goal(GoalType.GATHER_RESOURCE, 50);
        gatherGoal.targetBlockUri = "CoreAssets:Stone";
        goalSystem.addGoal(npc, gatherGoal);

        GoalComponent gc = npc.getComponent(GoalComponent.class);
        assertNotNull(gc);
        assertNotNull(gc.getActiveGoal());
        assertEquals(GoalType.GATHER_RESOURCE, gc.getActiveGoal().type);
    }
}
