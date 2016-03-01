package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class UnseenItemsMessageComposer extends MessageComposer {

    private final Map<Integer, List<Integer>> newObjects;

    public UnseenItemsMessageComposer(Map<Integer, List<Integer>> newObjects) {
        this.newObjects = newObjects;
    }

    public UnseenItemsMessageComposer(final Set<PlayerItem> PlayerItems) {
        this.newObjects = new HashMap<>();

        for (PlayerItem playerItem : PlayerItems) {
            if (!this.newObjects.containsKey(1)) {
                this.newObjects.put(1, Lists.newArrayList(ItemManager.getInstance().getItemVirtualId(playerItem.getId())));
            } else {
                this.newObjects.get(1).add(ItemManager.getInstance().getItemVirtualId(playerItem.getId()));
            }
        }
    }

    @Override
    public short getId() {
        return Composers.FurniListNotificationMessageComposer;
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
