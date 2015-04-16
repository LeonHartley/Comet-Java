package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


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
    public void compose(Composer msg) {
        msg.writeString(this.publicKey);
        msg.writeBoolean(false); // Client-side encryption... We don't need this.
    }
}
