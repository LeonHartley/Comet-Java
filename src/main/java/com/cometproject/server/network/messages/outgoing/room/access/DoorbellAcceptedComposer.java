package com.cometproject.server.network.messages.outgoing.room.access;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


/**
 * Created by Matty on 07/05/2014.
 */
public class DoorbellAcceptedComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.DoorbellOpenedMessageComposer);
        msg.writeString("");

        return msg;
    }
}
