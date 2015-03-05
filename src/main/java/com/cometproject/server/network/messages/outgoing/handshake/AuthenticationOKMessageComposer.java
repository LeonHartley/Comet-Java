package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class AuthenticationOKMessageComposer extends MessageComposer {
    public AuthenticationOKMessageComposer() {

    }

    @Override
    public short getId() {
        return Composers.AuthenticationOKMessageComposer;
    }

    @Override
    public void compose(Composer msg) {

    }
}
