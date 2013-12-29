package com.cometsrv.network.messages.outgoing.room.trading;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class TradeStartMessageComposer {
    public static Composer compose(int user1, int user2) {
        Composer msg = new Composer(Composers.TradeStartMessageComposer);

        msg.writeInt(user1);
        msg.writeInt(1);
        msg.writeInt(user2);
        msg.writeInt(1);

        return msg;
    }
}
