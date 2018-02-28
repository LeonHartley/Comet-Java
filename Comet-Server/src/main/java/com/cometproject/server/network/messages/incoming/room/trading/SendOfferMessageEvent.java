package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class SendOfferMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        long itemId = ItemManager.getInstance().getItemIdByVirtualId(msg.readInt());
        PlayerItem item = client.getPlayer().getInventory().getItem(itemId);

        if (item == null) {
            return;
        }

        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client.getPlayer().getEntity());
        if (trade == null) return;

        trade.addItem(trade.getUserNumber(client.getPlayer().getEntity()), item, true);
    }
}
