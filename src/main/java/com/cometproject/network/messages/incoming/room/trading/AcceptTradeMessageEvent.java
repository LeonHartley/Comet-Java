package com.cometproject.network.messages.incoming.room.trading;

import com.cometproject.game.rooms.types.components.types.Trade;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class AcceptTradeMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client);

        if(trade == null) {
            return;
        }

        trade.accept(trade.getUserNumber(client));
    }
}
