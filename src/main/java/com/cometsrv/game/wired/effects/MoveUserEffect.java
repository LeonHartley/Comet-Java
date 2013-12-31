package com.cometsrv.game.wired.effects;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.wired.data.WiredDataFactory;
import com.cometsrv.game.wired.data.effects.TeleportToItemData;
import com.cometsrv.game.wired.types.WiredEffect;
import com.cometsrv.network.messages.types.Event;


public class MoveUserEffect extends WiredEffect {
    @Override
    public void onActivate(Avatar avatar, FloorItem item) {
        // move user
    }

    @Override
    public void onSave(Event event, FloorItem item) {
        event.readInt(); // don't need this
        event.readString(); // don't need this

        int itemCount = event.readInt();
        TeleportToItemData instance = (TeleportToItemData) WiredDataFactory.get(item);

        if(instance == null) {
            return;
        }

        for(int i = 0; i < itemCount; i++) {
            instance.addItem(event.readInt());
        }

        WiredDataFactory.save(instance);
    }
}
