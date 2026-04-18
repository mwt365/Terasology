// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.components;

import org.terasology.gestalt.entitysystem.component.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Blackboard-style perception memory. NPCs write what they see, read when
 * deciding. Kept data-only; queries live on goals/nodes.
 *
 * Not intended for high-fidelity world modeling — this is a small cache of
 * recent percepts with timestamps so behaviors can reason about staleness.
 */
public class MemoryComponent implements Component<MemoryComponent> {

    /** Known resource sightings (opaque world positions — caller supplies type). */
    public List<Percept> resources = new ArrayList<>();

    /** Known threat sightings. */
    public List<Percept> threats = new ArrayList<>();

    /** Last-known safe retreat point, if any. */
    public Percept safeSpot;

    /** Generic k/v scratch for behaviors to coordinate across ticks. */
    public Map<String, Object> blackboard = new HashMap<>();

    /** Monotonic game-time timestamp of the most recent update (seconds). */
    public float lastUpdateTime;

    public void rememberResource(Percept p) {
        resources.add(p);
    }

    public void rememberThreat(Percept p) {
        threats.add(p);
    }

    public void forgetThreats() {
        threats.clear();
    }

    public boolean hasThreat() {
        return !threats.isEmpty();
    }

    public boolean hasResource() {
        return !resources.isEmpty();
    }

    public List<Percept> getThreats() {
        return Collections.unmodifiableList(threats);
    }

    public List<Percept> getResources() {
        return Collections.unmodifiableList(resources);
    }

    @Override
    public void copyFrom(MemoryComponent other) {
        this.resources = new ArrayList<>(other.resources);
        this.threats = new ArrayList<>(other.threats);
        this.safeSpot = other.safeSpot;
        this.blackboard = new HashMap<>(other.blackboard);
        this.lastUpdateTime = other.lastUpdateTime;
    }

    /** Lightweight percept: a tag + coordinates + when it was seen. */
    public static final class Percept {
        public final String tag;
        public final float x;
        public final float y;
        public final float z;
        public final float seenAt;

        public Percept(String tag, float x, float y, float z, float seenAt) {
            this.tag = tag;
            this.x = x;
            this.y = y;
            this.z = z;
            this.seenAt = seenAt;
        }
    }
}
