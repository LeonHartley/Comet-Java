package com.cometsrv.game.wired.triggers;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.wired.misc.WiredSquare;
import com.cometsrv.game.wired.types.WiredTrigger;
import com.cometsrv.network.messages.types.Event;

public class OnSayTrigger extends WiredTrigger {

    @Override
    public void onTrigger(Object data, Avatar user, WiredSquare wiredBlock) {
        if(!(data instanceof String))
            return;

        for(FloorItem item : user.getRoom().getItems().getItemsOnSquare(wiredBlock.getX(), wiredBlock.getY())) {
            // TODO: check for condition
            if(GameEngine.getWired().isWiredEffect(item)) {
                GameEngine.getWired().getEffect(item.getDefinition().getInteraction()).onActivate(user, item);
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
