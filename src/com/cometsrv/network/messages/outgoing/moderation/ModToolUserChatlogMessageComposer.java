package com.cometsrv.network.messages.outgoing.moderation;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class ModToolUserChatlogMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.ModToolUserChatlogMessageComposer);

        return msg;
    }
}
