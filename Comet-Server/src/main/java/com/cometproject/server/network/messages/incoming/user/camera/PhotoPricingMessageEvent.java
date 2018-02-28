package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.server.composers.camera.PhotoPricingMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class PhotoPricingMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new PhotoPricingMessageComposer(50, 50));// fetch from configuration
    }
}