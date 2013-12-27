package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class UserBadgesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();


    }
}
