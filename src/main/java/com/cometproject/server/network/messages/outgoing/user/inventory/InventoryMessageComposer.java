package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.items.rares.LimitedEditionItem;
import com.cometproject.server.game.players.components.InventoryComponent;
import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.lang.StringUtils;

public class InventoryMessageComposer {
    public static Composer compose(InventoryComponent inventory) {
        Composer msg = new Composer(Composers.LoadInventoryMessageComposer);

        msg.writeInt(1);
        msg.writeInt(1);
        msg.writeInt(inventory.getTotalSize());

        for (InventoryItem inventoryItem : inventory.getFloorItems().values()) {
            final boolean isGift = inventoryItem.getGiftData() != null;
            final boolean isGroupItem = inventoryItem.getDefinition().getInteraction().equals("group_item") || inventoryItem.getDefinition().getInteraction().equals("group_gate");
            final boolean isLimited = CometManager.getItems().getLimitedEdition(inventoryItem.getId()) != null;

            msg.writeInt(inventoryItem.getId());
            msg.writeString(inventoryItem.getDefinition().getType().toUpperCase());
            msg.writeInt(inventoryItem.getId());
            msg.writeInt(isGift ? inventoryItem.getGiftData().getSpriteId() : inventoryItem.getDefinition().getSpriteId());

            if(!isGroupItem)
                msg.writeInt(1);

            if (isGroupItem) {
                // Append the group data...
                int groupId = 0;

                msg.writeInt(17);

                if(StringUtils.isNumeric(inventoryItem.getExtraData())) {
                    groupId = Integer.parseInt(inventoryItem.getExtraData());
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
            } else if (isLimited) {
                msg.writeString("");
                msg.writeBoolean(true);
                msg.writeBoolean(false);
            } else if (inventoryItem.getDefinition().getInteraction().equals("badge_display") && !isGift) {
                msg.writeInt(2);
            } else {
                msg.writeInt(0);
            }

            if(inventoryItem.getDefinition().getInteraction().equals("badge_display") && !isGift) {
                msg.writeInt(4);

                msg.writeString("0");
                msg.writeString(inventoryItem.getExtraData());
                msg.writeString(""); // creator
                msg.writeString(""); // date
            } else if(!isGroupItem) {
                msg.writeString(!isGift ? inventoryItem.getExtraData() : "");
            }

            if(isLimited && !isGift) {
                LimitedEditionItem limitedEditionItem = CometManager.getItems().getLimitedEdition(inventoryItem.getId());

                msg.writeInt(limitedEditionItem.getLimitedRare());
                msg.writeInt(limitedEditionItem.getLimitedRareTotal());
            }

            msg.writeBoolean(inventoryItem.getDefinition().canRecycle);
            msg.writeBoolean(!isGift && inventoryItem.getDefinition().canTrade);
            msg.writeBoolean(!isLimited && !isGift && inventoryItem.getDefinition().canInventoryStack);
            msg.writeBoolean(!isGift && inventoryItem.getDefinition().canMarket);

            msg.writeInt(-1);
            msg.writeBoolean(false);//??
            msg.writeInt(-1);
            msg.writeString("");
            msg.writeInt(0);
        }

        // Wall items
        for (InventoryItem i : inventory.getWallItems().values()) {
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
