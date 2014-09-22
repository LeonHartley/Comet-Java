package com.cometproject.server.network.messages.outgoing.room.items.wired;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class WiredRewardMessageComposer {
    // 1-5 = error
    // 6-7 = success (rewardMisc, rewardBadge)
    public static Composer compose(int reason) {
        Composer msg = new Composer(Composers.WiredRewardMessageComposer);

        msg.writeInt(reason);

        return msg;
    }
}
