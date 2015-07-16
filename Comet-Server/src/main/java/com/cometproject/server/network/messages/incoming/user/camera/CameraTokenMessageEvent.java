package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class CameraTokenMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
//        client.send(new CameraTokenMessageComposer("Leon's Camera"));
    }
}