package com.cometproject.server.game.rooms.objects.items.events;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;

import java.util.concurrent.TimeUnit;

public abstract class AbstractItemEvent implements CometTask {
    private final RoomItemFloor floorItem;
    private final RoomEntity entity;

    public AbstractItemEvent(RoomItemFloor floorItem, RoomEntity entity) {
        this.floorItem = floorItem;
        this.entity = entity;
    }

    protected void runIn(double seconds) {
        CometThreadManager.getInstance().executeSchedule(this, (long) (seconds * 1000), TimeUnit.MILLISECONDS);
    }

    protected RoomItemFloor getFloorItem() {
        return floorItem;
    }

    protected RoomEntity getEntity() {
        return entity;
    }
}
