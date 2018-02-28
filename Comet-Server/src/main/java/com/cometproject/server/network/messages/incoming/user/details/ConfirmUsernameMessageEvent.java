package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class ConfirmUsernameMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final String username = msg.readString();

        if (client.getPlayer() != null) {
            if (client.getPlayer().getData().getUsername().equals(username)) {
                client.getPlayer().setUsernameConfirmed(true);
            }
        }
    }
}
