package com.cometproject.network.messages.incoming.messenger;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class SearchFriendsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String query = msg.readString();


    }
}
