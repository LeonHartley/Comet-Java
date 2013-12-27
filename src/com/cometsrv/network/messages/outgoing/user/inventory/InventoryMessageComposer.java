package com.cometsrv.network.messages.outgoing.user.inventory;

import com.cometsrv.game.players.components.InventoryComponent;
import com.cometsrv.game.players.components.types.InventoryItem;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class InventoryMessageComposer {
    public static Composer compose(InventoryComponent inv) {
        Composer msg = new Composer(Composers.InventoryMessageComposer);

        msg.writeInt(1);
        msg.writeInt(1);
        msg.writeInt(inv.getTotalSize());

        for(InventoryItem i : inv.getFloorItems().values()) {
            msg.writeInt(i.getId());
            msg.writeString(i.getDefinition().getType().toUpperCase());
            msg.writeInt(i.getId());
            msg.writeInt(i.getDefinition().getSpriteId());
            msg.writeInt(1);
            msg.writeString("");
            msg.writeInt(0);

            msg.writeBoolean(i.getDefinition().canRecycle);
            msg.writeBoolean(i.getDefinition().canTrade);
            msg.writeBoolean(i.getDefinition().canInventoryStack);
            msg.writeBoolean(i.getDefinition().canMarket);
            msg.writeInt(-1);
            msg.writeBoolean(true);
            msg.writeInt(-1);
            msg.writeString("");
            msg.writeInt(0);
        }

        for(InventoryItem i : inv.getWallItems().values()) {
            msg.writeInt(i.getId());
            msg.writeString(i.getDefinition().getType().toUpperCase());
            msg.writeInt(i.getId());
            msg.writeInt(i.getDefinition().getSpriteId());

            if(i.getDefinition().getItemName().contains("a2")) {
                msg.writeInt(3);
            } else if(i.getDefinition().getItemName().contains("wallpaper")) {
                msg.writeInt(2);
            } else if(i.getDefinition().getItemName().contains("landscape")) {
                msg.writeInt(4);
            } else {
                msg.writeInt(1);
            }

            msg.writeInt(0);
            msg.writeString(i.getExtraData());

            msg.writeBoolean(i.getDefinition().canRecycle);
            msg.writeBoolean(i.getDefinition().canTrade);
            msg.writeBoolean(i.getDefinition().canInventoryStack);
            msg.writeBoolean(i.getDefinition().canMarket);
            msg.writeInt(-1);
            msg.writeBoolean(true);
            msg.writeInt(-1);
        }

        return msg;
    }
}
