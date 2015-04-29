package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

import java.util.Map;


public class UnseenItemsMessageComposer extends MessageComposer {
    private final Map<Integer, Integer> items;

    public UnseenItemsMessageComposer(final Map<Integer, Integer> items) {
        this.items = items;
    }

    @Override
    public short getId() {
        return Composers.NewInventoryObjectMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(items.size());

        for (Map.Entry<Integer, Integer> i : items.entrySet()) {
            msg.writeInt(i.getKey());
            msg.writeInt(1);
            msg.writeInt(i.getValue());
        }
    }
}
