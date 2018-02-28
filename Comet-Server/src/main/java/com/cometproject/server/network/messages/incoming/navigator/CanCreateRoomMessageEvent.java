package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.CanCreateRoomMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class CanCreateRoomMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        client.send(new CanCreateRoomMessageComposer());
    }
}
