package com.cometsrv.network.messages.incoming.room.moderation;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class GetBannedUsersMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {

    }
}
