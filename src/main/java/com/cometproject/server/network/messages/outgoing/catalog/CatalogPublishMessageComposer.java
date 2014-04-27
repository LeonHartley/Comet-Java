package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

/**
 * Created by Matty on 27/04/2014.
 */
public class CatalogPublishMessageComposer {
    public static Composer compose(boolean b) {
        Composer msg = new Composer(Composers.CatalogPublishedMessageComposer);
        msg.writeBoolean(b);

        return msg;
    }
}
