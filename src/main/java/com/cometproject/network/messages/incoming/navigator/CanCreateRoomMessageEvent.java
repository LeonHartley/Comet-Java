package com.cometproject.network.messages.incoming.navigator;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.navigator.CanCreateRoomMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class CanCreateRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(CanCreateRoomMessageComposer.compose());
    }
}
