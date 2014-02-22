package com.cometproject.network.messages.incoming.room.trading;

import com.cometproject.boot.Comet;
import com.cometproject.game.rooms.types.components.types.Trade;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

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
