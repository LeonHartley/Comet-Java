package com.cometproject.game.wired.types;

import com.cometproject.game.rooms.entities.types.PlayerEntity;
import com.cometproject.game.rooms.items.FloorItem;
import com.cometproject.game.wired.misc.WiredSquare;
import com.cometproject.network.messages.types.Event;

public abstract class WiredTrigger {
    public abstract void onTrigger(Object data, PlayerEntity user, WiredSquare wiredBlock);
    public abstract void onSave(Event event, FloorItem item);
}
