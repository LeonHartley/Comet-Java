package com.cometproject.server.game.wired.types;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.network.messages.types.Event;

public abstract class WiredEffect {
    public abstract void onActivate(PlayerEntity avatar, FloorItem item);
    public abstract void onSave(Event event, FloorItem item);
}
