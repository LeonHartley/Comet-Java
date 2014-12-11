package com.cometproject.server.network.messages.incoming.user.username;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.username.ChangeUsernameCheckMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class ChangeUsernameCheckMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        String username = msg.readString();

        if (client.getPlayer().getData().getUsername().equals(username)) {
            client.send(ChangeUsernameCheckMessageComposer.compose(false, username));
            return;
        }

        boolean isAvailable = PlayerDao.usernameIsAvailable(username);
        client.send(ChangeUsernameCheckMessageComposer.compose(isAvailable, username));
    }
}
