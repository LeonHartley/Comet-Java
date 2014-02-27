package com.cometproject.server.game.wired.types;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;

public abstract class WiredEffect {
    public abstract void onActivate(List<PlayerEntity> entities, FloorItem item);
    public abstract void onSave(Event event, FloorItem item);
}
