package com.cometproject.server.game.wired.triggers;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.misc.WiredSquare;
import com.cometproject.server.game.wired.types.WiredTrigger;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;

public class OnSayTrigger extends WiredTrigger {

    @Override
    public void onTrigger(Object data, List<PlayerEntity> entity, WiredSquare wiredBlock) {
        if(!(data instanceof String))
            return;

        Room room = entity.get(0).getRoom();

        for(FloorItem item : room.getItems().getItemsOnSquare(wiredBlock.getX(), wiredBlock.getY())) {
            // TODO: check for condition
            if(GameEngine.getWired().isWiredEffect(item)) {
                GameEngine.getWired().getEffect(item.getDefinition().getInteraction()).onActivate(entity, item);
            }
        }
    }

    @Override
    public void onSave(Event msg, FloorItem item) {
        msg.readInt(); msg.readInt(); // 2nd int = isOwner

        String chat = msg.readString();

        item.setExtraData(chat);
        item.saveData();
    }
}
