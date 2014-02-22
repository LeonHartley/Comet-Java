package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class TradeCompleteMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.TradeCompleteMessageComposer);

        return msg;
    }
}
