// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.events;

import org.joml.Vector3f;
import org.terasology.gestalt.entitysystem.event.Event;

public class ResourceFoundEvent implements Event {
    private final String blockUri;
    private final Vector3f position;

    public ResourceFoundEvent(String blockUri, Vector3f position) {
        this.blockUri = blockUri;
        this.position = position;
    }

    public String getBlockUri() {
        return blockUri;
    }

    public Vector3f getPosition() {
        return position;
    }
}
