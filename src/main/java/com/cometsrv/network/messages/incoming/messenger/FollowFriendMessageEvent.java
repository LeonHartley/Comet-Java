package com.cometsrv.network.messages.incoming.messenger;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class FollowFriendMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int friendId = msg.readInt();
    }
}