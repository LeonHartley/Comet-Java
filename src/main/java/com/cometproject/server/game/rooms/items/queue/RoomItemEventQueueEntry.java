package com.cometproject.server.game.rooms.items.queue;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItem;

public class RoomItemEventQueueEntry {
    private final RoomItem item;
    private final GenericEntity entity;

    private final RoomItemEventType type;

    // optional
    private final int requestData;
    private final boolean isWiredTrigger;

    public RoomItemEventQueueEntry(RoomItem item, RoomItemEventType type) {
        this.item = item;
        this.entity = null;

        this.type = type;

        this.requestData = -1;
        this.isWiredTrigger = false;
    }

    public RoomItemEventQueueEntry(RoomItem item, GenericEntity entity, RoomItemEventType type) {
        this.item = item;
        this.entity = entity;

        this.type = type;

        this.requestData = -1;
        this.isWiredTrigger = false;
    }

    public RoomItemEventQueueEntry(RoomItem item, GenericEntity entity, RoomItemEventType type, int requestData, boolean isWiredTrigger) {
        this.item = item;
        this.entity = entity;

        this.type = type;

        this.requestData = requestData;
        this.isWiredTrigger = isWiredTrigger;
    }

    public RoomItem getItem() {
        return item;
    }

    public GenericEntity getEntity() {
        return entity;
    }

    public RoomItemEventType getType() {
        return type;
    }

    public int getRequestData() {
        return requestData;
    }

    public boolean isWiredTrigger() {
        return isWiredTrigger;
    }
}
