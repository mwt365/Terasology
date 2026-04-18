// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.events;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.event.Event;
import org.terasology.module.advancednpcai.model.ThreatLevel;

public class ThreatDetectedEvent implements Event {
    private final EntityRef threatEntity;
    private final Vector3f threatPosition;
    private final ThreatLevel level;

    public ThreatDetectedEvent(EntityRef threatEntity, Vector3f threatPosition, ThreatLevel level) {
        this.threatEntity = threatEntity;
        this.threatPosition = threatPosition;
        this.level = level;
    }

    public EntityRef getThreatEntity() {
        return threatEntity;
    }

    public Vector3f getThreatPosition() {
        return threatPosition;
    }

    public ThreatLevel getLevel() {
        return level;
    }
}
