package com.cometproject.network.messages.incoming.handshake;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.handshake.SecretKeyMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class GenerateSecretKeyMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        client.send(SecretKeyMessageComposer.compose("24231219992253632572058933470468103090824667747608911151318774416044820318109"));
    }
}
