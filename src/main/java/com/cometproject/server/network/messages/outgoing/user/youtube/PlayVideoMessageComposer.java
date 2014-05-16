package com.cometproject.server.network.messages.outgoing.user.youtube;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class PlayVideoMessageComposer {
    public static Composer compose(int itemId, String videoId, int videoLength) {
        Composer msg = new Composer(Composers.PlayVideoMessageComposer);

        msg.writeInt(itemId);
        msg.writeString(videoId);
        msg.writeInt(0);
        msg.writeInt(videoLength);

        return msg;
    }
}
