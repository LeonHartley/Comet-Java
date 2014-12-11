package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class SearchFriendsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String query = msg.readString();

        client.send(client.getPlayer().getMessenger().search(query));
    }
}
