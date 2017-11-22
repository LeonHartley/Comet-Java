package com.cometproject.server.composers.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class CatalogPublishMessageComposer extends MessageComposer {

    private final boolean showNotification;

    public CatalogPublishMessageComposer(final boolean showNotification) {
        this.showNotification = showNotification;
    }

    @Override
    public short getId() {
        return Composers.CatalogUpdatedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(this.showNotification);
    }
}
