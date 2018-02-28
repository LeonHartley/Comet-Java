package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class IgnoreInvitationsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        boolean ignoreRoomInvitations = msg.readBoolean();

        client.getPlayer().getSettings().setIgnoreInvites(ignoreRoomInvitations);
        PlayerDao.saveIgnoreInvitations(ignoreRoomInvitations, client.getPlayer().getId());
    }
}
