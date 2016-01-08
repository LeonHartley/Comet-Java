package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class GiftUserNotFoundMessageComposer extends MessageComposer {

    public GiftUserNotFoundMessageComposer() {

    }

    @Override
    public short getId() {
        return Composers.GiftWrappingErrorMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
