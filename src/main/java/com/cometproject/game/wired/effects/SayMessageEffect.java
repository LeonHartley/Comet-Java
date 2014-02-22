package com.cometproject.game.wired.effects;

import com.cometproject.game.rooms.entities.types.PlayerEntity;
import com.cometproject.game.rooms.items.FloorItem;
import com.cometproject.game.wired.types.WiredEffect;
import com.cometproject.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometproject.network.messages.types.Event;

public class SayMessageEffect extends WiredEffect {
    @Override
    public void onActivate(PlayerEntity entity, FloorItem item) {
        entity.getPlayer().getSession().send(WisperMessageComposer.compose(entity.getVirtualId(), item.getExtraData()));
    }

    @Override
    public void onSave(Event event, FloorItem item) {
        event.readInt();
        String msg = event.readString();

        item.setExtraData(msg);
        item.saveData();
    }
}
