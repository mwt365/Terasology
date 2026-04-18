// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.model;

public enum ThreatLevel {
    NONE(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4);

    private final int severity;

    ThreatLevel(int severity) {
        this.severity = severity;
    }

    public int getSeverity() {
        return severity;
    }
}
