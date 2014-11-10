package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class UpdateIgnoreStatusMessageComposer {
    public static Composer compose(int result, String username) {
        Composer msg = new Composer(Composers.UpdateIgnoreStatusMessageComposer);

        msg.writeInt(result);
        msg.writeString(username);

        return msg;
    }
}
