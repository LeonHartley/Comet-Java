package com.cometproject.server.game.rooms.objects.items.types.floor.wired.events;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.state.FloorItemEvent;

public class WiredItemEvent extends FloorItemEvent {

    public RoomEntity entity;

    public WiredItemEvent() {
        super(0);
    }
}
