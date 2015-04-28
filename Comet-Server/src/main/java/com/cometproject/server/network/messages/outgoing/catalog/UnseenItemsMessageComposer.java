package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class UnseenItemsMessageComposer extends MessageComposer {

    private final List<InventoryItem> inventoryItems;

    public UnseenItemsMessageComposer(final List<InventoryItem> items) {
        this.inventoryItems = items;
    }

    @Override
    public short getId() {
        return Composers.NewInventoryObjectMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        int inventoryTab = 1;
        for (InventoryItem item : this.inventoryItems) {
            if (!item.getDefinition().getType().equals("s"))
                inventoryTab = 2;
        }

        msg.writeInt(1); // count of tabs that need updating
        msg.writeInt(inventoryTab); // tab id

        msg.writeInt(this.inventoryItems.size());

        for (InventoryItem item : this.inventoryItems) {
            msg.writeInt(item.getId());
        }
    }

    @Override
    public void dispose() {
        this.inventoryItems.clear();
    }
}
