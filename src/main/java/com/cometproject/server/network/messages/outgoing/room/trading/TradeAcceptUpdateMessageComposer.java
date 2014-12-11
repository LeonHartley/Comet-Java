package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class TradeAcceptUpdateMessageComposer {
    public static Composer compose(int userId, boolean accepted) {
        Composer msg = new Composer(Composers.TradeAcceptMessageComposer);

        msg.writeInt(userId);
        msg.writeInt(accepted ? 1 : 0);

        return msg;
    }
}
