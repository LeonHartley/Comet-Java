package com.cometproject.server.composers.handshake;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


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
