package com.cometsrv.game.wired.types;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.wired.misc.WiredSquare;
import com.cometsrv.network.messages.types.Event;

public abstract class WiredTrigger {
    public abstract void onTrigger(Object data, Avatar user, WiredSquare wiredBlock);
    public abstract void onSave(Event event, FloorItem item);
}
