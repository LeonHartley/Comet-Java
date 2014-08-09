package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ModToolUserAlertMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int playerId = msg.readInt();
        String message = msg.readString();

        if(!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            // fuck off
            client.getLogger().error(
                    ModToolUserInfoMessageEvent.class.getName() + " - tried to alert user: " + playerId + " with text: " + message);
            client.disconnect();
            return;
        }

        if (CometManager.getPlayers().isOnline(playerId)) {
            Session session = Comet.getServer().getNetwork().getSessions().getByPlayerId(playerId);

            if (session != null) {
                session.send(AdvancedAlertMessageComposer.compose(message));
            }
        }
    }
}
