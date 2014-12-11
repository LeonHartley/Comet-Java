package com.cometproject.server.network.messages.outgoing.user.purse;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class SendCreditsMessageComposer {
    public static Composer compose(int credits) {
        Composer msg = new Composer(Composers.CreditsBalanceMessageComposer);

        msg.writeString(credits + ".0");

        return msg;
    }
}
