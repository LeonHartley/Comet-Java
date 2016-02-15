package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class TradingOfferItemsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int amount = msg.readInt();

        if(amount > 100) {
            amount = 100;
        }

        final long itemId = ItemManager.getInstance().getItemIdByVirtualId(msg.readInt());

        InventoryItem item = client.getPlayer().getInventory().getFloorItem(itemId);

        if (item == null) {
            item = client.getPlayer().getInventory().getWallItem(itemId);

            if (item == null) {
                return;
            }
        }

        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client.getPlayer().getEntity());
        if (trade == null) return;

        int i = 0;

        for(InventoryItem inventoryItem : client.getPlayer().getInventory().getFloorItems().values()) {
            if(i >= amount)
                break;

            if (inventoryItem.getBaseId() == item.getBaseId() && !trade.isOffered(inventoryItem)) {
                i++;

                trade.addItem(trade.getUserNumber(client.getPlayer().getEntity()), inventoryItem, false);
            }
        }

        trade.updateWindow();
    }
}
