// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.components;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.module.advancednpcai.model.ThreatLevel;

public class AIComponent implements Component<AIComponent> {
    @Replicate
    public boolean active = true;

    @Replicate
    public ThreatLevel currentThreatLevel = ThreatLevel.NONE;

    @Replicate
    public float detectionRange = 20f;

    @Replicate
    public float healthFleeThreshold = 0.3f;

    @Replicate
    public float reevaluationInterval = 1.0f;

    public transient float timeSinceLastEvaluation = 0f;

    @Override
    public void copyFrom(AIComponent other) {
        this.active = other.active;
        this.currentThreatLevel = other.currentThreatLevel;
        this.detectionRange = other.detectionRange;
        this.healthFleeThreshold = other.healthFleeThreshold;
        this.reevaluationInterval = other.reevaluationInterval;
    }
}
