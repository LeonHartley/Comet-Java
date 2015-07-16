package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UnseenItemsMessageComposer extends MessageComposer {

    private final Map<Integer, List<Integer>> newObjects;

    public UnseenItemsMessageComposer(Map<Integer, List<Integer>> newObjects) {
        this.newObjects = newObjects;
    }

    public UnseenItemsMessageComposer(final List<InventoryItem> inventoryItems) {
        this.newObjects = new HashMap<>();

        for (InventoryItem inventoryItem : inventoryItems) {
            if (!this.newObjects.containsKey(1)) {
                this.newObjects.put(1, Lists.newArrayList(inventoryItem.getId()));
            } else {
                this.newObjects.get(1).add(inventoryItem.getId());
            }
        }
    }

    @Override
    public short getId() {
        return Composers.NewInventoryObjectMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.newObjects.size());

        for (Map.Entry<Integer, List<Integer>> tab : this.newObjects.entrySet()) {
            msg.writeInt(tab.getKey());
            msg.writeInt(tab.getValue().size());

            for (Object item : tab.getValue()) {
                msg.writeInt((Integer) item);
            }
        }
    }

    @Override
    public void dispose() {
        for (Map.Entry<Integer, List<Integer>> tab : this.newObjects.entrySet()) {
            tab.getValue().clear();
        }

        this.newObjects.clear();
    }
}
