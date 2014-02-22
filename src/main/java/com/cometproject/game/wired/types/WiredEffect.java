package com.cometproject.game.wired.types;

import com.cometproject.game.rooms.entities.types.PlayerEntity;
import com.cometproject.game.rooms.items.FloorItem;
import com.cometproject.network.messages.types.Event;

public abstract class WiredEffect {
    public abstract void onActivate(PlayerEntity avatar, FloorItem item);
    public abstract void onSave(Event event, FloorItem item);
}
