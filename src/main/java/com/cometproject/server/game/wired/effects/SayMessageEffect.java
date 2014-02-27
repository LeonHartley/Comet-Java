package com.cometproject.server.game.wired.effects;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.wired.types.WiredEffect;
import com.cometproject.server.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;

public class SayMessageEffect extends WiredEffect {
    @Override
    public void onActivate(List<PlayerEntity> entities, FloorItem item) {
        for(PlayerEntity entity : entities)
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
