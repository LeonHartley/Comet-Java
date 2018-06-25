package com.cometproject.server.game.rooms.objects.entities;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;

public class WiredTriggerExecutor<T extends WiredTriggerItem> implements CometTask {
    private final RoomEntity roomEntity;

    private final Class<? extends WiredTriggerItem> triggerClass;
    private final Object data;

    public WiredTriggerExecutor(Class<T> triggerClass, RoomEntity entity, Object data) {
        this.roomEntity = entity;
        this.data = data;
        this.triggerClass = triggerClass;

    }

    @Override
    public void run() {
        for (RoomItemFloor wiredItem : WiredTriggerItem.getTriggers(this.roomEntity.getRoom(), this.triggerClass)) {
            T trigger = ((T) wiredItem);

            if (trigger.getWiredData().getSelectedIds().contains(((RoomItemFloor) data).getId()))
                trigger.evaluate(this.roomEntity, data);
        }
    }
}
