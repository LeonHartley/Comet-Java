package com.cometproject.server.game.wired.types;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;

public abstract class WiredEffect {
    public abstract void onActivate(List<GenericEntity> entities, RoomItemFloor item);

    public abstract void onSave(Event event, RoomItemFloor item);
}
