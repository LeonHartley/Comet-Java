package com.cometproject.network.messages.outgoing.room.trading;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class TradeCloseMessageComposer {
    public static Composer compose(int userId) {
        Composer msg = new Composer(Composers.TradeCloseMessageComposer);

        msg.writeInt(userId);
        msg.writeInt(2);

        return msg;
    }
}
