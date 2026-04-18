// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.model;

import org.joml.Vector3f;

public class MemoryEntry {
    public String key;
    public Vector3f position;
    public float timestamp;
    public float expiresAfter;

    public MemoryEntry() {
    }

    public MemoryEntry(String key, Vector3f position, float timestamp, float expiresAfter) {
        this.key = key;
        this.position = position != null ? new Vector3f(position) : null;
        this.timestamp = timestamp;
        this.expiresAfter = expiresAfter;
    }

    public MemoryEntry copy() {
        return new MemoryEntry(key, position, timestamp, expiresAfter);
    }
}
