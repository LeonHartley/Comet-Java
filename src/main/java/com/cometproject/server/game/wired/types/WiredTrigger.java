package com.cometproject.server.game.wired.types;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.wired.misc.WiredSquare;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;

public abstract class WiredTrigger {
    public abstract void onTrigger(Object data, List<GenericEntity> entities, WiredSquare wiredBlock);

    public abstract void onSave(Event event, FloorItem item);
}
