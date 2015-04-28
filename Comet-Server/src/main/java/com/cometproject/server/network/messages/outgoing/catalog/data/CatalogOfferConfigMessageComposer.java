package com.cometproject.server.network.messages.outgoing.catalog.data;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class CatalogOfferConfigMessageComposer extends MessageComposer {

    public CatalogOfferConfigMessageComposer() {

    }

    @Override
    public short getId() {
        return Composers.CatalogOfferConfigMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(100);
        msg.writeInt(6);
        msg.writeInt(1);
        msg.writeInt(1);
        msg.writeInt(2);
        msg.writeInt(40);
        msg.writeInt(99);
    }
}
