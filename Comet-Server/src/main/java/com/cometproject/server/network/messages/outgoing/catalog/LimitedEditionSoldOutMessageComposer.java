package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class LimitedEditionSoldOutMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.CatalogLimitedItemSoldOutMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
