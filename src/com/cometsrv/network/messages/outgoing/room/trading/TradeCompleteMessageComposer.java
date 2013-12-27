package com.cometsrv.network.messages.outgoing.room.trading;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class TradeCompleteMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.TradeCompleteMessageComposer);

        return msg;
    }
}
