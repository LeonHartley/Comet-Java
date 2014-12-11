package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class CatalogPublishMessageComposer {
    public static Composer compose(boolean b) {
        Composer msg = new Composer(Composers.PublishShopMessageComposer);
        msg.writeBoolean(b);

        return msg;
    }
}
