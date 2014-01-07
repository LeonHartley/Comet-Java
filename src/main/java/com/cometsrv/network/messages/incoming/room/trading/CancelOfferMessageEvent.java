package com.cometsrv.network.messages.incoming.room.trading;

import com.cometsrv.game.players.components.types.InventoryItem;
import com.cometsrv.game.rooms.types.components.types.Trade;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class CancelOfferMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();
        InventoryItem item = client.getPlayer().getInventory().getFloorItem(itemId);

        if(item == null) {
            item = client.getPlayer().getInventory().getWallItem(itemId);
        }

        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client);
        trade.removeItem(trade.getUserNumber(client), item);
    }
}
