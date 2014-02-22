package com.cometproject.network.messages.outgoing.catalog;

import com.cometproject.game.players.components.types.InventoryItem;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

import java.util.List;
import java.util.Map;

public class SendPurchaseAlertMessageComposer {
    public static Composer compose(List<InventoryItem> items) {
        Composer msg = new Composer(Composers.SendPurchaseAlertMessageComposer);

        int i = 1;
        for(InventoryItem item : items) {
            if(!item.getDefinition().getType().equals("s"))
                i = 2;
        }

        msg.writeInt(1);
        msg.writeInt(i);
        msg.writeInt(items.size());

        for(InventoryItem item : items) {
            msg.writeInt(item.getId());
        }

        return msg;
    }

    public static Composer compose(Map<Integer, Integer> items) {
        Composer msg = new Composer(Composers.SendPurchaseAlertMessageComposer);

        int i = 1;
        for(Integer item : items.values()) {
            if(item == 2)
                i = 2;
        }

        msg.writeInt(1);
        msg.writeInt(i);
        msg.writeInt(items.size());

        for(Integer item : items.keySet()) {
            msg.writeInt(item);
        }

        return msg;
    }
}
