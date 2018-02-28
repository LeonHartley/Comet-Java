package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class ModToolUserKickMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (!client.getPlayer().getPermissions().getRank().modTool()) {
            // fuck off
            client.disconnect();
            return;
        }

        int playerId = msg.readInt();
        String message = msg.readString();

        if (PlayerManager.getInstance().isOnline(playerId)) {
            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

            if (session != null) {
                if (!message.isEmpty())
                    session.send(new AdvancedAlertMessageComposer(message));

                if (session.getPlayer().getPermissions().getRank().roomKickable() && session.getPlayer().getEntity() != null) {
                    session.getPlayer().getEntity().kick();
                }
            }
        }
    }
}
