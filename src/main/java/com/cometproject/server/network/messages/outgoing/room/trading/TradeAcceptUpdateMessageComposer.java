package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class TradeAcceptUpdateMessageComposer {
    public static Composer compose(int userId) {
        Composer msg = new Composer(Composers.TradeAcceptUpdateMessageComposer);

        msg.writeInt(userId);
        msg.writeInt(1);

        return msg;
    }
}
