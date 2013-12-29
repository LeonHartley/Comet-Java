package com.cometsrv.network.messages.outgoing.user.inventory;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

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
