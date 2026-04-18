// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.components;

import org.junit.jupiter.api.Test;
import org.terasology.module.advancednpcai.model.ThreatLevel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AIComponentTest {

    @Test
    public void testCopyFrom() {
        AIComponent source = new AIComponent();
        source.active = false;
        source.currentThreatLevel = ThreatLevel.HIGH;
        source.detectionRange = 30f;
        source.healthFleeThreshold = 0.5f;
        source.reevaluationInterval = 2.0f;

        AIComponent target = new AIComponent();
        target.copyFrom(source);

        assertFalse(target.active);
        assertEquals(ThreatLevel.HIGH, target.currentThreatLevel);
        assertEquals(30f, target.detectionRange);
        assertEquals(0.5f, target.healthFleeThreshold);
        assertEquals(2.0f, target.reevaluationInterval);
    }

    @Test
    public void testDefaultValues() {
        AIComponent ai = new AIComponent();
        assertTrue(ai.active);
        assertEquals(ThreatLevel.NONE, ai.currentThreatLevel);
        assertEquals(20f, ai.detectionRange);
        assertEquals(0.3f, ai.healthFleeThreshold);
        assertEquals(1.0f, ai.reevaluationInterval);
    }
}
