// Copyright 2026 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.module.advancednpcai.systems;

import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.module.advancednpcai.components.AIComponent;
import org.terasology.module.advancednpcai.components.MemoryComponent;
import org.terasology.module.advancednpcai.events.ResourceFoundEvent;
import org.terasology.module.advancednpcai.model.MemoryEntry;

import java.util.Iterator;

@RegisterSystem(RegisterMode.AUTHORITY)
@Share(MemorySystem.class)
public class MemorySystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    @In
    private EntityManager entityManager;
    @In
    private Time time;

    @Override
    public void update(float delta) {
        float gameTime = time.getGameTime();
        for (EntityRef entity : entityManager.getEntitiesWith(MemoryComponent.class, AIComponent.class)) {
            MemoryComponent mc = entity.getComponent(MemoryComponent.class);
            boolean changed = false;
            Iterator<MemoryEntry> it = mc.entries.iterator();
            while (it.hasNext()) {
                MemoryEntry e = it.next();
                if (e.expiresAfter > 0 && (gameTime - e.timestamp) > e.expiresAfter) {
                    it.remove();
                    changed = true;
                }
            }
            if (changed) {
                entity.saveComponent(mc);
            }
        }
    }

    public void remember(EntityRef entity, MemoryEntry entry) {
        MemoryComponent mc = entity.getComponent(MemoryComponent.class);
        if (mc == null) {
            return;
        }
        entry.timestamp = time.getGameTime();
        mc.entries.removeIf(e -> e.key.equals(entry.key));
        if (mc.entries.size() >= mc.maxEntries) {
            mc.entries.remove(0);
        }
        mc.entries.add(entry);
        entity.saveComponent(mc);
    }

    public MemoryEntry recall(EntityRef entity, String keyPrefix) {
        MemoryComponent mc = entity.getComponent(MemoryComponent.class);
        if (mc == null) {
            return null;
        }
        for (int i = mc.entries.size() - 1; i >= 0; i--) {
            if (mc.entries.get(i).key.startsWith(keyPrefix)) {
                return mc.entries.get(i);
            }
        }
        return null;
    }

    @ReceiveEvent(components = {MemoryComponent.class, AIComponent.class})
    public void onResourceFound(ResourceFoundEvent event, EntityRef entity) {
        remember(entity, new MemoryEntry(
                "resource_" + event.getBlockUri(),
                event.getPosition(),
                time.getGameTime(),
                300f
        ));
    }
}
