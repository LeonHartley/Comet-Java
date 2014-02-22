package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ModToolUserChatlogMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.ModToolUserChatlogMessageComposer);

        return msg;
    }
}
