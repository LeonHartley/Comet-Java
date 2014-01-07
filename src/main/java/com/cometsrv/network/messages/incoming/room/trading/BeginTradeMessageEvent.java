package com.cometsrv.network.messages.incoming.room.trading;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.rooms.types.components.types.Trade;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class BeginTradeMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        Session user = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId);

        if(user == null) {
            return;
        }

        client.getPlayer().getEntity().getRoom().getTrade().add(new Trade(client, user));
    }
}
