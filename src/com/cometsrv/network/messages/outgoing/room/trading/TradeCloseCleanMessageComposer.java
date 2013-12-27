package com.cometsrv.network.messages.outgoing.room.trading;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class TradeCloseCleanMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.TradeCloseCleanMessageComposer);

        return msg;
    }
}
