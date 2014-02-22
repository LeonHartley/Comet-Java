package com.cometproject.network.messages.incoming.room.action;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class UserBadgesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();


    }
}
