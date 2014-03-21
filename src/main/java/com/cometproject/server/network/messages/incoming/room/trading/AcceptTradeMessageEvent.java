package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class AcceptTradeMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client.getPlayer().getEntity());

        if(trade == null) {
            return;
        }

        trade.accept(trade.getUserNumber(client.getPlayer().getEntity()));
    }
}
