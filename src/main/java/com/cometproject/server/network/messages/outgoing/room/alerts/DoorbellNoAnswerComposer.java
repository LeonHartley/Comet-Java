package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

/**
 * Created by Matty on 07/05/2014.
 */
public class DoorbellNoAnswerComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.DoorBellNoAnswerComposer);
        msg.writeString("");

        return msg;
    }
}
