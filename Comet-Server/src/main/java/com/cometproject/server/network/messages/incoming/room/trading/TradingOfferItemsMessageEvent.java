package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.api.game.players.data.components.inventory.IInventoryItem;
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

        IInventoryItem item = client.getPlayer().getInventory().getFloorItem(itemId);

        if (item == null) {
            item = client.getPlayer().getInventory().getWallItem(itemId);

            if (item == null) {
                return;
            }
        }

        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client.getPlayer().getEntity());
        if (trade == null) return;

        int i = 0;

        for(IInventoryItem IInventoryItem : client.getPlayer().getInventory().getFloorItems().values()) {
            if(i >= amount)
                break;

            if (IInventoryItem.getBaseId() == item.getBaseId() && !trade.isOffered(IInventoryItem)) {
                i++;

                trade.addItem(trade.getUserNumber(client.getPlayer().getEntity()), IInventoryItem, false);
            }
        }

        trade.updateWindow();
    }
}
