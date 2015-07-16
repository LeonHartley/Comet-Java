package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class CatalogPublishMessageComposer extends MessageComposer {

    private final boolean showNotification;

    public CatalogPublishMessageComposer(final boolean showNotification) {
        this.showNotification = showNotification;
    }

    @Override
    public short getId() {
        return Composers.CatalogPublishMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(this.showNotification);
    }
}
