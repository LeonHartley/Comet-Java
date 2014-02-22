package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class UserBadgesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();


    }
}
