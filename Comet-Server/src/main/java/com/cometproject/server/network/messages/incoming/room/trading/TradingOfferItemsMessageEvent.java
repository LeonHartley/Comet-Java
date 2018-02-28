package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class TradingOfferItemsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int amount = msg.readInt();

        if (amount > 100) {
            amount = 100;
        }

        final long itemId = ItemManager.getInstance().getItemIdByVirtualId(msg.readInt());

        PlayerItem item = client.getPlayer().getInventory().getItem(itemId);
        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client.getPlayer().getEntity());
        if (trade == null) return;

        int i = 0;

        for (PlayerItem playerItem : client.getPlayer().getInventory().getInventoryItems().values()) {
            if (i >= amount)
                break;

            if (playerItem.getBaseId() == item.getBaseId() && !trade.isOffered(playerItem)) {
                i++;

                trade.addItem(trade.getUserNumber(client.getPlayer().getEntity()), playerItem, false);
            }
        }

        trade.updateWindow();
    }
}
