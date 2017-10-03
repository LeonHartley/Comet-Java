package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.camera.PhotoPricingMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

import static com.cometproject.server.protocol.headers.Composers.PhotoPriceMessageComposer;


public class PhotoPricingMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new PhotoPricingMessageComposer(50, 50));// fetch from configuration
    }
}