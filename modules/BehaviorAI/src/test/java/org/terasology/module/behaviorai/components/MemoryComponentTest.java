// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoryComponentTest {

    @Test
    void threatsAndResourcesTrackPresence() {
        MemoryComponent m = new MemoryComponent();
        assertFalse(m.hasThreat());
        assertFalse(m.hasResource());

        m.rememberResource(new MemoryComponent.Percept("tree", 1, 0, 1, 0.5f));
        m.rememberThreat(new MemoryComponent.Percept("zombie", 4, 0, 5, 0.6f));
        assertTrue(m.hasThreat());
        assertTrue(m.hasResource());

        m.forgetThreats();
        assertFalse(m.hasThreat());
        assertTrue(m.hasResource());
    }

    @Test
    void copyFromProducesIndependentLists() {
        MemoryComponent a = new MemoryComponent();
        a.rememberResource(new MemoryComponent.Percept("tree", 1, 2, 3, 0f));
        a.rememberThreat(new MemoryComponent.Percept("wolf", 4, 5, 6, 0f));
        a.blackboard.put("foo", 7);

        MemoryComponent b = new MemoryComponent();
        b.copyFrom(a);

        assertEquals(1, b.resources.size());
        assertEquals(1, b.threats.size());
        assertEquals(7, b.blackboard.get("foo"));

        a.resources.clear();
        a.threats.clear();
        a.blackboard.clear();
        assertEquals(1, b.resources.size(), "copyFrom must deep-copy the resources list");
        assertEquals(1, b.threats.size());
        assertEquals(7, b.blackboard.get("foo"));
    }
}
