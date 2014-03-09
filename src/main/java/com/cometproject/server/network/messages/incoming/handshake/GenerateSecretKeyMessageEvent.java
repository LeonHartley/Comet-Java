package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.SecretKeyMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GenerateSecretKeyMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        client.send(SecretKeyMessageComposer.compose("24231219992253632572058933470468103090824667747608911151318774416044820318109"));
    }
}
