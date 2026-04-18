// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.actions.combat;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.module.health.events.DoDamageEvent;

@BehaviorAction(name = "defend")
public class DefendAction extends BaseAction {
    private int attackDamage = 5;
    private float attackRange = 2f;
    private float attackCooldown = 1.0f;

    @Override
    public void construct(Actor actor) {
        actor.setValue(getId(), 0f);
    }

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        Float cooldown = actor.getValue(getId());
        if (cooldown == null) {
            cooldown = 0f;
        }
        cooldown -= actor.getDelta();
        actor.setValue(getId(), cooldown);

        if (cooldown > 0) {
            return BehaviorState.RUNNING;
        }

        EntityRef threat = actor.readFromBlackboard("threatEntity");
        if (threat == null || threat == EntityRef.NULL) {
            return BehaviorState.FAILURE;
        }

        LocationComponent npcLoc = actor.getComponent(LocationComponent.class);
        LocationComponent threatLoc = threat.getComponent(LocationComponent.class);
        if (npcLoc == null || threatLoc == null) {
            return BehaviorState.FAILURE;
        }

        float dist = npcLoc.getLocalPosition().distance(threatLoc.getLocalPosition());
        if (dist > attackRange) {
            return BehaviorState.FAILURE;
        }

        threat.send(new DoDamageEvent(attackDamage));
        actor.setValue(getId(), attackCooldown);
        return BehaviorState.RUNNING;
    }
}
