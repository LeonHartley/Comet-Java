package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.players.components.InventoryComponent;
import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import org.apache.commons.lang.StringUtils;

public class InventoryMessageComposer {
    public static Composer compose(InventoryComponent inv) {
        Composer msg = new Composer(Composers.LoadInventoryMessageComposer);

        msg.writeInt(1);
        msg.writeInt(1);
        msg.writeInt(inv.getTotalSize());

        for (InventoryItem i : inv.getFloorItems().values()) {
            boolean isGift = false;
            boolean isGroupItem = i.getDefinition().getInteraction().equals("group_item") || i.getDefinition().getInteraction().equals("group_gate");

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
            } else if(isGroupItem) {
                int groupId = 0;

                msg.writeInt(17);

                if(StringUtils.isNumeric(i.getExtraData())) {
                    groupId = Integer.parseInt(i.getExtraData());
                }

                GroupData groupData = CometManager.getGroups().getData(groupId);

                if(groupData == null) {
                    msg.writeInt(0);
                } else {
                    msg.writeInt(2);
                    msg.writeInt(5);
                    msg.writeString("0");
                    msg.writeString(groupId);
                    msg.writeString(groupData.getBadge());

                    String colourA = CometManager.getGroups().getGroupItems().getSymbolColours().get(groupData.getColourA()).getColour();
                    String colourB = CometManager.getGroups().getGroupItems().getBackgroundColours().get(groupData.getColourB()).getColour();

                    msg.writeString(colourA);
                    msg.writeString(colourB);
                }
            } else {
                msg.writeInt(isGift ? 9 : 0);
                msg.writeInt(0);
                msg.writeString(isGift ? "" : i.getExtraData());
            }

            msg.writeBoolean(i.getDefinition().canRecycle);
            msg.writeBoolean(!isGift && i.getDefinition().canTrade);
            msg.writeBoolean(!isGift && i.getDefinition().canInventoryStack);
            msg.writeBoolean(!isGift && i.getDefinition().canMarket);

            msg.writeInt(-1);
            msg.writeBoolean(true);
            msg.writeInt(-1);
            msg.writeString("");

            int extra = 0;

            if(isGift) {
                extra = (i.getGiftData().getDecorationType() * 1000) + i.getGiftData().getWrappingPaper();
            }

            msg.writeInt(extra);
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
