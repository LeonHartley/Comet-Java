package com.cometproject.gamecenter.fastfood.net.composers;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;

public class AuthenticationOKMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return 11;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
