package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class SecretKeyMessageComposer extends MessageComposer {
    private final String publicKey;

    public SecretKeyMessageComposer(final String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public short getId() {
        return Composers.SecretKeyMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.publicKey);
    }
}
