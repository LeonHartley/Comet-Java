package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class MutedMessageComposer {
    public static Composer compose(int secondsLeft) {
        final Composer msg = new Composer(Composers.MutedMessageComposer);

        msg.writeInt(secondsLeft);

        return msg;
    }
}
