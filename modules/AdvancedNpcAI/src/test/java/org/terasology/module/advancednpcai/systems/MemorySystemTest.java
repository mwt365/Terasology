// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.systems;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.integrationenvironment.jupiter.IntegrationEnvironment;
import org.terasology.engine.registry.In;
import org.terasology.module.advancednpcai.components.AIComponent;
import org.terasology.module.advancednpcai.components.MemoryComponent;
import org.terasology.module.advancednpcai.model.MemoryEntry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@IntegrationEnvironment(dependencies = "AdvancedNpcAI")
public class MemorySystemTest {
    @In
    protected EntityManager entityManager;
    @In
    protected MemorySystem memorySystem;

    private EntityRef createNpc() {
        EntityRef npc = entityManager.create();
        npc.addComponent(new AIComponent());
        npc.addComponent(new MemoryComponent());
        return npc;
    }

    @Test
    public void testRememberAndRecall() {
        EntityRef npc = createNpc();
        MemoryEntry entry = new MemoryEntry("resource_stone", new Vector3f(10, 20, 30), 0f, -1f);
        memorySystem.remember(npc, entry);

        MemoryEntry recalled = memorySystem.recall(npc, "resource_");
        assertNotNull(recalled);
        assertEquals("resource_stone", recalled.key);
    }

    @Test
    public void testRecallReturnsNullForMissing() {
        EntityRef npc = createNpc();
        assertNull(memorySystem.recall(npc, "nonexistent_"));
    }

    @Test
    public void testRememberReplacesExistingKey() {
        EntityRef npc = createNpc();
        memorySystem.remember(npc, new MemoryEntry("resource_stone", new Vector3f(1, 2, 3), 0f, -1f));
        memorySystem.remember(npc, new MemoryEntry("resource_stone", new Vector3f(4, 5, 6), 0f, -1f));

        MemoryComponent mc = npc.getComponent(MemoryComponent.class);
        assertEquals(1, mc.entries.size());
        assertEquals(4f, mc.entries.get(0).position.x);
    }

    @Test
    public void testMaxEntriesEviction() {
        EntityRef npc = createNpc();
        MemoryComponent mc = npc.getComponent(MemoryComponent.class);
        mc.maxEntries = 3;
        npc.saveComponent(mc);

        for (int i = 0; i < 5; i++) {
            memorySystem.remember(npc, new MemoryEntry("entry_" + i, new Vector3f(i, 0, 0), 0f, -1f));
        }

        mc = npc.getComponent(MemoryComponent.class);
        assertEquals(3, mc.entries.size());
        assertEquals("entry_2", mc.entries.get(0).key);
    }
}
