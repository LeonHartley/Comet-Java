package com.cometproject.server.game.rooms.objects.items.types.floor.wired.events;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.state.FloorItemEvent;

public class WiredItemEvent extends FloorItemEvent {
    public int type = 0;
    public RoomEntity entity;
    public Object data;

    public WiredItemEvent(RoomEntity entity, Object data) {
        super(0);

        this.entity = entity;
        this.data = data;
    }
}
