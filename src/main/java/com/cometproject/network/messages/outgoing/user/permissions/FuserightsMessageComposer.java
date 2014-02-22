package com.cometproject.network.messages.outgoing.user.permissions;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class FuserightsMessageComposer {
    public static Composer compose(boolean hasClub, int rank) {
        Composer msg = new Composer(Composers.FuserightsMessageComposer);

        msg.writeInt(hasClub ? 2 : 0);
        msg.writeInt(rank);

        return msg;
    }
}
