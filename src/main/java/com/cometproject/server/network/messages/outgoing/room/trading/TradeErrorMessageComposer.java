package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class TradeErrorMessageComposer {
    public static Composer compose(int errorCode, String extras) {
        Composer msg = new Composer(Composers.TradeErrorMessageComposer);

        msg.writeInt(errorCode);
        msg.writeString(extras);

        return msg;
    }
}
