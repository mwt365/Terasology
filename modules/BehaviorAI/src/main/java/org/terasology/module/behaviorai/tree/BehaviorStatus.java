// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.behaviorai.tree;

public enum BehaviorStatus {
    SUCCESS,
    FAILURE,
    RUNNING;

    public boolean isFinished() {
        return this == SUCCESS || this == FAILURE;
    }
}
