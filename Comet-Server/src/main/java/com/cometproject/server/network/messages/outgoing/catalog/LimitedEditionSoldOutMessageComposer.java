package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class LimitedEditionSoldOutMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.CatalogLimitedItemSoldOutMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
