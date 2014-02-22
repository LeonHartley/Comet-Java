package com.cometproject.server.game.wired.triggers;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.wired.misc.WiredSquare;
import com.cometproject.server.game.wired.types.WiredTrigger;
import com.cometproject.server.network.messages.types.Event;

public class EnterRoomTrigger extends WiredTrigger {
    @Override
    public void onTrigger(Object data, PlayerEntity user, WiredSquare wiredBlock) {

        for(FloorItem item : user.getRoom().getItems().getItemsOnSquare(wiredBlock.getX(), wiredBlock.getY())) {
            // TODO: check for condition.
            if(GameEngine.getWired().isWiredEffect(item)) {
                GameEngine.getWired().getEffect(item.getDefinition().getInteraction()).onActivate(user, item);
            }
        }
    }

    @Override
    public void onSave(Event event, FloorItem item) {
        int mode = event.readInt();
        String username = event.readString();

        item.setExtraData(username);
        item.saveData();
    }
}
