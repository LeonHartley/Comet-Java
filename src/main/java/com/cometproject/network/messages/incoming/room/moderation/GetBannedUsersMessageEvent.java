package com.cometproject.network.messages.incoming.room.moderation;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class GetBannedUsersMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {

    }
}
