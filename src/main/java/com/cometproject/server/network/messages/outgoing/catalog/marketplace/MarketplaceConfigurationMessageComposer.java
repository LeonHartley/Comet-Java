package com.cometproject.server.network.messages.outgoing.catalog.marketplace;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class MarketplaceConfigurationMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.MarketplaceConfigurationMessageComposer);

        msg.writeBoolean(true);
        msg.writeInt(100);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(1000000);
        msg.writeInt(48);
        msg.writeInt(7);

        return msg;
    }
}
