package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class EnableTradingMessageComposer {
    public static Composer compose(boolean enableTrading) {
        Composer msg = new Composer(Composers.EnableTradingMessageComposer);

        msg.writeBoolean(enableTrading);

        return msg;
    }
}
