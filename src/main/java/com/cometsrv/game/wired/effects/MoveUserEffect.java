package com.cometsrv.game.wired.effects;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.wired.types.WiredEffect;
import com.cometsrv.network.messages.types.Event;

public class MoveUserEffect extends WiredEffect {
    @Override
    public void onActivate(Avatar avatar, FloorItem item) {
        // move user
    }

    @Override
    public void onSave(Event event, FloorItem item) {
        GameEngine.getLogger().debug("We are tracking furni: " + event.readInt());
    }
}
