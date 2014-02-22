package com.cometproject.network.messages.outgoing.room.trading;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class TradeCloseCleanMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.TradeCloseCleanMessageComposer);

        return msg;
    }
}
