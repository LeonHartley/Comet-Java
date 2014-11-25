package com.cometproject.server.network.messages.outgoing.room.items.postit;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class PostItMessageComposer {
    public static Composer compose(int id, String data) {
        Composer msg = new Composer(Composers.LoadPostItMessageComposer);

        msg.writeString(id + "");
        msg.writeString(data);

        return msg;
    }
}
