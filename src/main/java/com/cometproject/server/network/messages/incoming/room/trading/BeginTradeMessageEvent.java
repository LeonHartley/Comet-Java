package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class BeginTradeMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        PlayerEntity entity = (PlayerEntity)client.getPlayer().getEntity().getRoom().getEntities().getEntity(userId);

        if(entity == null) {
            return;
        }

        client.getPlayer().getEntity().getRoom().getTrade().add(new Trade(client.getPlayer().getEntity(), entity));
    }
}
