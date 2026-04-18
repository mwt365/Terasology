// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.conditions;

import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.module.health.components.HealthComponent;

@BehaviorAction(name = "is_health_low")
public class IsHealthLowAction extends BaseAction {
    private float threshold = 0.3f;

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        HealthComponent health = actor.getComponent(HealthComponent.class);
        if (health == null) {
            return BehaviorState.FAILURE;
        }
        float ratio = (float) health.currentHealth / health.maxHealth;
        return ratio <= threshold ? BehaviorState.SUCCESS : BehaviorState.FAILURE;
    }
}
