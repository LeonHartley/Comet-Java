package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class CancelOfferMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        long itemId = ItemManager.getInstance().getItemIdByVirtualId(msg.readInt());

        PlayerItem item = client.getPlayer().getInventory().getItem(itemId);

        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client.getPlayer().getEntity());
        if (trade == null) return;

        trade.removeItem(trade.getUserNumber(client.getPlayer().getEntity()), item);
    }
}
