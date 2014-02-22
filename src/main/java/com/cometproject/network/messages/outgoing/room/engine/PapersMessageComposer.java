package com.cometproject.network.messages.outgoing.room.engine;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class PapersMessageComposer {
    public static Composer compose(String key, String value) {
        Composer msg = new Composer(Composers.PapersMessageComposer);

        msg.writeString(key);
        msg.writeString(value);

        return msg;
    }
}
