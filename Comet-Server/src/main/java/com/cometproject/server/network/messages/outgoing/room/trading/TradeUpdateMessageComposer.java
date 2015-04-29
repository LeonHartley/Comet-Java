package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

import java.util.List;


public class TradeUpdateMessageComposer extends MessageComposer {

    private final int user1;
    private final int user2;
    private final List<InventoryItem> items1;
    private final List<InventoryItem> items2;

    public TradeUpdateMessageComposer(int user1, int user2, List<InventoryItem> items1, List<InventoryItem> items2) {
        this.user1 = user1;
        this.user2 = user2;
        this.items1 = items1;
        this.items2 = items2;
    }

    @Override
    public short getId() {
        return Composers.TradeUpdateMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
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
    }
}
