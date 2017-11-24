package com.cometproject.server.composers.handshake;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


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
