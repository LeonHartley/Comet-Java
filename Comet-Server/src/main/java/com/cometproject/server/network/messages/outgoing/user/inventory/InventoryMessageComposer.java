package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.game.players.data.components.PlayerInventory;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Map;


public class InventoryMessageComposer extends MessageComposer {
    public static final int ITEMS_PER_PAGE = 2000;

    private final int pageCount;
    private final int currentPage;
    private final Map<Long, PlayerItem> inventoryItems;

    public InventoryMessageComposer(int pageCount, int currentPage, Map<Long, PlayerItem> inventoryItems) {
        this.pageCount = pageCount;
        this.currentPage = currentPage;
        this.inventoryItems = inventoryItems;
    }

    @Override
    public short getId() {
        return Composers.FurniListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.pageCount); // how many pages
        msg.writeInt(this.currentPage); // index of current page
        msg.writeInt(this.inventoryItems.size());

        for (Map.Entry<Long, PlayerItem>  inventoryItem : this.inventoryItems.entrySet()) {
            inventoryItem.getValue().compose(msg);
        }
    }
}
