package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

import java.util.ArrayList;
import java.util.List;


public class UnseenItemsMessageComposer extends MessageComposer {

    private final List<Integer> tabs;
    private final List<Object> newItems;

    public UnseenItemsMessageComposer(final List<Integer> tabs, final List<Object> items) {
        this.tabs = tabs;
        this.newItems = items;
    }

    public UnseenItemsMessageComposer(final List<InventoryItem> inventoryItems) {
        this.tabs = new ArrayList<>();
        this.newItems = new ArrayList<>();

        for(InventoryItem inventoryItem : inventoryItems) {
            if(!inventoryItem.getDefinition().getType().toLowerCase().equals("s") && !this.tabs.contains(2)) {
                this.tabs.add(2);
            } else {
                if(!this.tabs.contains(1)) {
                    this.tabs.add(1);
                }
            }
        }
    }

    @Override
    public short getId() {
        return Composers.NewInventoryObjectMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(tabs.size());

        for(int tabId : this.tabs) {
            msg.writeInt(tabId);
        }

        msg.writeInt(this.newItems.size());

        for(Object obj : this.newItems) {
            if(obj instanceof Integer) {
                msg.writeInt((Integer) obj);
            } else if(obj instanceof String) {
                msg.writeString(obj);
            }
        }
    }

    @Override
    public void dispose() {
        this.newItems.clear();
    }
}
