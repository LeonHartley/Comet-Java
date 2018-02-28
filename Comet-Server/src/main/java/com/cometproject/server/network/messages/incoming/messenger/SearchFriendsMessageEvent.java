package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class SearchFriendsMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String query = msg.readString();

        client.send(client.getPlayer().getMessenger().search(query));
    }
}
