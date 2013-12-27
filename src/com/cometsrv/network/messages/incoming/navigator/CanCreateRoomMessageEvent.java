package com.cometsrv.network.messages.incoming.navigator;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.navigator.CanCreateRoomMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class CanCreateRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(CanCreateRoomMessageComposer.compose());
    }
}
