package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ModToolUserKickMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if(!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            // fuck off
            client.disconnect();
            return;
        }

        int playerId = msg.readInt();
        String message = msg.readString();

        if (CometManager.getPlayers().isOnline(playerId)) {
            Session session = Comet.getServer().getNetwork().getSessions().getByPlayerId(playerId);

            if (session != null) {
                if (!message.isEmpty())
                    session.send(AdvancedAlertMessageComposer.compose(message));

                if (session.getPlayer().getEntity() != null) {
                    session.getPlayer().getEntity().leaveRoom(false, true, true);
                }
            }
        }
    }
}
