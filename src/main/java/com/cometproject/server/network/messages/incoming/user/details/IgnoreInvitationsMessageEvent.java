package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class IgnoreInvitationsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        boolean ignoreRoomInvitations = msg.readBoolean();

        client.getPlayer().getSettings().setIgnoreInvites(ignoreRoomInvitations);
        PlayerDao.saveIgnoreInvitations(ignoreRoomInvitations, client.getPlayer().getId());
    }
}
