package com.cometsrv.network.messages.incoming.room.trading;

import com.cometsrv.game.players.components.types.InventoryItem;
import com.cometsrv.game.rooms.types.components.types.Trade;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

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

        Trade trade = client.getPlayer().getAvatar().getRoom().getTrade().get(client);
        trade.addItem(trade.getUserNumber(client), item);
    }
}
