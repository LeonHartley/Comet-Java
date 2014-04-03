package com.cometproject.server.game.wired.effects;


import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.wired.types.WiredEffect;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;

import java.util.List;

public class KickUserEffect extends WiredEffect {
    @Override
    public void onActivate(List<PlayerEntity> entities, FloorItem item) {
        for(PlayerEntity entity : entities) {
            if(entity.getRoom().getData().getOwnerId() != entity.getPlayer().getId() && !entity.getPlayer().getPermissions().hasPermission("user_unkickable"))
                return;
            else
                entity.leaveRoom(false, true, true);
            entity.getPlayer().getSession().send(AdvancedAlertMessageComposer.compose("Alert", item.getExtraData()));

        }
    }

    @Override
    public void onSave(Event event, FloorItem item) {
        event.readInt();
        String msg = event.readString();

        item.setExtraData(msg);
        item.saveData();
    }
}
