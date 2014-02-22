package com.cometproject.network.messages.outgoing.moderation;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class ModToolUserChatlogMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.ModToolUserChatlogMessageComposer);

        return msg;
    }
}
