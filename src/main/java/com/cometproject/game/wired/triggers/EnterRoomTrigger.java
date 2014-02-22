package com.cometproject.game.wired.triggers;

import com.cometproject.game.GameEngine;
import com.cometproject.game.rooms.entities.types.PlayerEntity;
import com.cometproject.game.rooms.items.FloorItem;
import com.cometproject.game.wired.misc.WiredSquare;
import com.cometproject.game.wired.types.WiredTrigger;
import com.cometproject.network.messages.types.Event;

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
