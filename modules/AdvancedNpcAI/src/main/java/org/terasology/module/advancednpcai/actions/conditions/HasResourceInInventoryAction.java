// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.conditions;

import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;

@BehaviorAction(name = "has_resource_in_inventory")
public class HasResourceInInventoryAction extends BaseAction {
    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        Integer count = actor.readFromBlackboard("inventoryResourceCount");
        if (count != null && count > 0) {
            return BehaviorState.SUCCESS;
        }
        return BehaviorState.FAILURE;
    }
}
