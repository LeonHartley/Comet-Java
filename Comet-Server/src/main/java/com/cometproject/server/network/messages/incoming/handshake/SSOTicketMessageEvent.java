package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class SSOTicketMessageEvent implements Event {
    public static final String TICKET_DELIMITER = ":";

    public void handle(Session client, MessageEvent msg) {
        if (BanManager.getInstance().hasBan(client.getUniqueId(), BanType.MACHINE)) {
            client.getLogger().warn("Banned player: " + client.getUniqueId() + " tried logging in");
            return;
        }

        String ticket = msg.readString();

//        if (ticket.length() < 10 || ticket.length() > 128) {
//            client.getLogger().warn("Session was disconnected because ticket was too long or too short. Length: " + ticket.length());
//            client.disconnect();
//            return;
//        }

        PlayerManager.getInstance().submitLoginRequest(client, ticket);
    }
}
