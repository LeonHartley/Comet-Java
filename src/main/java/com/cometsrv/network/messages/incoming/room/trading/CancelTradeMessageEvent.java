package com.cometsrv.network.messages.incoming.room.trading;

import com.cometsrv.game.rooms.types.components.types.Trade;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class CancelTradeMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client);

        if(trade == null) {
            return;
        }

        trade.cancel(client.getPlayer().getId());
    }
}
