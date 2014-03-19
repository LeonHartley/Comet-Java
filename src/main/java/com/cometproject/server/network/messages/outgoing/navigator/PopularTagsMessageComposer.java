package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class PopularTagsMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.PopularTagsMessageComposer);

        msg.writeInt(0);
        // TODO: show tags

        return msg;
    }
}
