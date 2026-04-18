// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.conditions;

import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.module.advancednpcai.model.ThreatLevel;

@BehaviorAction(name = "is_threatened")
public class IsThreatenedAction extends BaseAction {
    private String minThreatLevel = "LOW";

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        String threatLevel = actor.readFromBlackboard("threatLevel");
        if (threatLevel == null) {
            return BehaviorState.FAILURE;
        }
        try {
            ThreatLevel current = ThreatLevel.valueOf(threatLevel);
            ThreatLevel threshold = ThreatLevel.valueOf(minThreatLevel);
            return current.getSeverity() >= threshold.getSeverity()
                    ? BehaviorState.SUCCESS : BehaviorState.FAILURE;
        } catch (IllegalArgumentException e) {
            return BehaviorState.FAILURE;
        }
    }
}
