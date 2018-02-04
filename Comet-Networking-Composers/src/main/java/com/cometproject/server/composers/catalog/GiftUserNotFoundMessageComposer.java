package com.cometproject.server.composers.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


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
