// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.components;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

public class NpcTypeComponent implements Component<NpcTypeComponent> {
    @Replicate
    public String npcType = "gatherer";

    @Override
    public void copyFrom(NpcTypeComponent other) {
        this.npcType = other.npcType;
    }
}
