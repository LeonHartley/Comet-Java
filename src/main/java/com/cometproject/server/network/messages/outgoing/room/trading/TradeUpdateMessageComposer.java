package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class TradeUpdateMessageComposer {
    public static Composer compose(int user1, int user2, List<InventoryItem> items1, List<InventoryItem> items2) {
        Composer msg = new Composer(Composers.TradeUpdateMessageComposer);

        msg.writeInt(user1);
        msg.writeInt(items1.size());

        for (InventoryItem item : items1) {
            item.serializeTrade(msg);
        }

        msg.writeInt(user2);
        msg.writeInt(items2.size());

        for (InventoryItem item : items2) {
            item.serializeTrade(msg);
        }

        return msg;
    }
}
