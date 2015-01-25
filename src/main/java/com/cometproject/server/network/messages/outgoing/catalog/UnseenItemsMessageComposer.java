package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class UnseenItemsMessageComposer {
    public static Composer compose(List<InventoryItem> items) {
        Composer msg = new Composer(Composers.NewInventoryObjectMessageComposer);

        int inventoryTab = 1;
        for (InventoryItem item : items) {
            if (!item.getDefinition().getType().equals("s"))
                inventoryTab = 2;
        }

        msg.writeInt(1); // count of tabs that need updating
        msg.writeInt(inventoryTab); // tab id

        msg.writeInt(items.size());

        for (InventoryItem item : items) {
            msg.writeInt(item.getId());
        }

        return msg;
    }
}
