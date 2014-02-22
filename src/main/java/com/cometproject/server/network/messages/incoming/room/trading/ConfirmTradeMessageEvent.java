package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ConfirmTradeMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client);

        if(trade == null) {
            return;
        }

        trade.confirm(trade.getUserNumber(client), client.getPlayer().getEntity().getRoom().getTrade());
    }
}
