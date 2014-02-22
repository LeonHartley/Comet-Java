package com.cometproject.network.messages.outgoing.user.inventory;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

import java.util.Map;

public class UnseenItemsMessageComposer {
    public static Composer compose(Map<Integer, Integer> items) {
        Composer msg = new Composer(Composers.UnseenItemsMessageComposer);

        msg.writeInt(items.size());

        for(Map.Entry<Integer, Integer> i : items.entrySet()) {
            msg.writeInt(i.getKey());
            msg.writeInt(1);
            msg.writeInt(i.getValue());
        }

        return msg;
    }
}
