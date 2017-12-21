package com.cometproject.server.composers.handshake;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class InitCryptoMessageComposer extends MessageComposer {
    private final String prime;
    private final String generator;

    public InitCryptoMessageComposer(final String prime, final String generator) {
        this.prime = prime;
        this.generator = generator;
    }

    @Override
    public short getId() {
        return Composers.InitCryptoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.prime);
        msg.writeString(this.generator);
    }
}
