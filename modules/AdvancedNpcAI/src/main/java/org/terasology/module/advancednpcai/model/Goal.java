// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.model;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityRef;

public class Goal {
    public GoalType type;
    public int priority;
    public EntityRef targetEntity = EntityRef.NULL;
    public Vector3f targetPosition;
    public String targetBlockUri;
    public boolean completed;
    public float timeStarted;

    public Goal() {
    }

    public Goal(GoalType type, int priority) {
        this.type = type;
        this.priority = priority;
    }

    public Goal copy() {
        Goal g = new Goal();
        g.type = this.type;
        g.priority = this.priority;
        g.targetEntity = this.targetEntity;
        g.targetPosition = this.targetPosition != null ? new Vector3f(this.targetPosition) : null;
        g.targetBlockUri = this.targetBlockUri;
        g.completed = this.completed;
        g.timeStarted = this.timeStarted;
        return g;
    }
}
