package com.cometsrv.game.wired.effects;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.wired.types.WiredEffect;
import com.cometsrv.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometsrv.network.messages.types.Event;

public class SayMessageEffect extends WiredEffect {
    @Override
    public void onActivate(Avatar avatar, FloorItem item) {
        avatar.getPlayer().getSession().send(WisperMessageComposer.compose(avatar.getPlayer().getId(), item.getExtraData()));
    }

    @Override
    public void onSave(Event event, FloorItem item) {
        event.readInt();
        String msg = event.readString();

        item.setExtraData(msg);
        item.saveData();
    }
}
