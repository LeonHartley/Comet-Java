package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.camera.CameraTokenMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class CameraTokenMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.send(CameraTokenMessageComposer.compose("67152056372501114565316423057012359"));
    }
}
