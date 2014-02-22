package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class FollowFriendMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int friendId = msg.readInt();
    }
}