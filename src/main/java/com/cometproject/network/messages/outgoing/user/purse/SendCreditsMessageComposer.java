package com.cometproject.network.messages.outgoing.user.purse;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class SendCreditsMessageComposer {
    public static Composer compose(int credits) {
        Composer msg = new Composer(Composers.SendCreditsMessageComposer);

        msg.writeString(credits + ".0");

        return msg;
    }
}
