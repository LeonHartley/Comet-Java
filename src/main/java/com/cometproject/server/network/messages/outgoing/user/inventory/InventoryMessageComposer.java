package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.game.players.components.InventoryComponent;
import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class InventoryMessageComposer {
    public static Composer compose(InventoryComponent inv) {
        Composer msg = new Composer(Composers.InventoryMessageComposer);

        msg.writeInt(1);
        msg.writeInt(1);
        msg.writeInt(inv.getTotalSize());

        for (InventoryItem i : inv.getFloorItems().values()) {
            boolean isGift = false;

            if (i.getGiftData() != null) {
                isGift = true;
            }

            msg.writeInt(i.getId());
            msg.writeString(i.getDefinition().getType().toUpperCase());
            msg.writeInt(i.getId());
            msg.writeInt(isGift ? i.getGiftData().getSpriteId() : i.getDefinition().getSpriteId());

            if (i.getDefinition().getInteraction().equals("badge_display") && !isGift) {
                msg.writeInt(0);
                msg.writeInt(2);
                msg.writeInt(4);

                msg.writeString(i.getExtraData());
                msg.writeString(i.getExtraData());
                msg.writeString(i.getExtraData());
                msg.writeString(i.getExtraData());
            } else {
                msg.writeInt(1);
                msg.writeInt(0);
                msg.writeString(isGift ? i.getGiftData().toString() : i.getExtraData());
            }

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

        for (InventoryItem i : inv.getWallItems().values()) {
            msg.writeInt(i.getId());
            msg.writeString(i.getDefinition().getType().toUpperCase());
            msg.writeInt(i.getId());
            msg.writeInt(i.getDefinition().getSpriteId());

            if (i.getDefinition().getItemName().contains("a2")) {
                msg.writeInt(3);
            } else if (i.getDefinition().getItemName().contains("wallpaper")) {
                msg.writeInt(2);
            } else if (i.getDefinition().getItemName().contains("landscape")) {
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
            msg.writeBoolean(false);
            msg.writeInt(-1);
        }

        return msg;
    }
}
