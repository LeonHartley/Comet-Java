package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class AuthenticationOKMessageComposer extends MessageComposer {
    public AuthenticationOKMessageComposer() {

    }

    @Override
    public short getId() {
        return Composers.AuthenticationOKMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
