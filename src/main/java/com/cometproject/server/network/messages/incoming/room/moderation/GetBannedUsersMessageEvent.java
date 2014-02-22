package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GetBannedUsersMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {

    }
}
