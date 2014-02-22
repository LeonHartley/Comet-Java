package com.cometproject.network.messages.incoming.room.trading;

import com.cometproject.game.players.components.types.InventoryItem;
import com.cometproject.game.rooms.types.components.types.Trade;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class SendOfferMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();
        InventoryItem item = client.getPlayer().getInventory().getFloorItem(itemId);

        if(item == null) {
            item = client.getPlayer().getInventory().getWallItem(itemId);

            if(item == null) {
                return;
            }
        }

        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client);
        trade.addItem(trade.getUserNumber(client), item);
    }
}
