package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GiveHandItemMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int userId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        PlayerEntity providerEntity = client.getPlayer().getEntity();
        PlayerEntity receivingEntity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPlayerId(userId);

        if (receivingEntity == null) {
            return;
        }

        receivingEntity.carryItem(providerEntity.getHandItem());
        providerEntity.carryItem(0);
    }
}
