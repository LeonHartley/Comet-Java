package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class TradeConfirmationMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.TradeConfirmationMessageComposer);

        return msg;
    }
}
