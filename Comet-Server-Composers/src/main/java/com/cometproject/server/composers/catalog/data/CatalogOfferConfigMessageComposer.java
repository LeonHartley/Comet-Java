package com.cometproject.server.composers.catalog.data;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class CatalogOfferConfigMessageComposer extends MessageComposer {

    public CatalogOfferConfigMessageComposer() {

    }

    @Override
    public short getId() {
        return Composers.CatalogItemDiscountMessageComposer;
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
