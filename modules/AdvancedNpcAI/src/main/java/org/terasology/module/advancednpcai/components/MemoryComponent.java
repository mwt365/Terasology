// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.components;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.module.advancednpcai.model.MemoryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryComponent implements Component<MemoryComponent> {
    @Replicate
    public List<MemoryEntry> entries = new ArrayList<>();

    @Replicate
    public Vector3f lastThreatPosition;

    public EntityRef threatEntity = EntityRef.NULL;

    @Replicate
    public int maxEntries = 50;

    @Override
    public void copyFrom(MemoryComponent other) {
        this.entries = other.entries.stream().map(MemoryEntry::copy).collect(Collectors.toList());
        this.lastThreatPosition = other.lastThreatPosition != null ? new Vector3f(other.lastThreatPosition) : null;
        this.threatEntity = other.threatEntity;
        this.maxEntries = other.maxEntries;
    }
}
