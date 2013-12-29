package com.cometsrv.game.wired.types;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.network.messages.types.Event;

public abstract class WiredEffect {
    public abstract void onActivate(Avatar avatar, FloorItem item);
    public abstract void onSave(Event event, FloorItem item);
}
