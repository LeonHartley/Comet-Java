package com.cometsrv.network.messages.outgoing.room.items.wired;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class WiredTriggerMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.WiredTriggerMessageComposer);
        //TODO this
        return msg;
    }
}
