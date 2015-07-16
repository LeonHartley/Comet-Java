package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class SendOfferMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int itemId = msg.readInt();
        InventoryItem item = client.getPlayer().getInventory().getFloorItem(itemId);

        if (item == null) {
            item = client.getPlayer().getInventory().getWallItem(itemId);

            if (item == null) {
                return;
            }
        }

        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client.getPlayer().getEntity());
        if (trade == null) return;

        trade.addItem(trade.getUserNumber(client.getPlayer().getEntity()), item);
    }
}
