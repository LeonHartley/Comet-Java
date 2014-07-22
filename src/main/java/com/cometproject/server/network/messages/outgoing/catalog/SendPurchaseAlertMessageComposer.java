package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class SendPurchaseAlertMessageComposer {
    public static Composer compose(List<InventoryItem> items) {
        Composer msg = new Composer(Composers.SendPurchaseAlertMessageComposer);

        int i = 1;
        for (InventoryItem item : items) {
            if (!item.getDefinition().getType().equals("s"))
                i = 2;
        }

        msg.writeInt(1);
        msg.writeInt(i);
        msg.writeInt(items.size());

        for (InventoryItem item : items) {
            msg.writeInt(item.getId());
        }

        return msg;
    }

    public static Composer compose() {
        Composer msg = new Composer(Composers.SendPurchaseAlertMessageComposer);


        msg.writeInt(1);
        msg.writeInt(0);
        msg.writeInt(0);

        return msg;
    }
}
