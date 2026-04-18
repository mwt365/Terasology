// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.components;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import org.terasology.module.advancednpcai.model.MemoryEntry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MemoryComponentTest {

    @Test
    public void testCopyFromDeepCopiesEntries() {
        MemoryComponent source = new MemoryComponent();
        source.entries.add(new MemoryEntry("resource_stone", new Vector3f(10, 20, 30), 100f, 300f));
        source.lastThreatPosition = new Vector3f(5, 5, 5);
        source.maxEntries = 25;

        MemoryComponent target = new MemoryComponent();
        target.copyFrom(source);

        assertEquals(1, target.entries.size());
        assertEquals("resource_stone", target.entries.get(0).key);
        assertEquals(25, target.maxEntries);
        assertNotSame(source.entries.get(0), target.entries.get(0));
        assertNotNull(target.lastThreatPosition);
        assertNotSame(source.lastThreatPosition, target.lastThreatPosition);
    }

    @Test
    public void testCopyFromHandlesNullThreatPosition() {
        MemoryComponent source = new MemoryComponent();
        source.lastThreatPosition = null;

        MemoryComponent target = new MemoryComponent();
        target.lastThreatPosition = new Vector3f(1, 2, 3);
        target.copyFrom(source);

        assertNull(target.lastThreatPosition);
    }

    @Test
    public void testDefaultValues() {
        MemoryComponent mc = new MemoryComponent();
        assertNotNull(mc.entries);
        assertEquals(0, mc.entries.size());
        assertEquals(50, mc.maxEntries);
        assertNull(mc.lastThreatPosition);
    }
}
