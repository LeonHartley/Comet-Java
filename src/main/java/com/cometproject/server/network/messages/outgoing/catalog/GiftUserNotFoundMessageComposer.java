package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class GiftUserNotFoundMessageComposer extends MessageComposer {

    public GiftUserNotFoundMessageComposer() {

    }

    @Override
    public short getId() {
        return Composers.GiftUserNotFoundMessageComposer;
    }

    @Override
    public void compose(Composer msg) {

    }
}
