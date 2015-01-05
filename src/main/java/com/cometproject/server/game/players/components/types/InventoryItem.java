package com.cometproject.server.game.players.components.types;

import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.rares.LimitedEditionItem;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.items.LimitedEditionDao;
import com.cometproject.server.utilities.JsonFactory;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;


public class InventoryItem {
    private int id;
    private int baseId;
    private String extraData;
    private GiftData giftData;

    private LimitedEditionItem limitedEditionItem;

    public InventoryItem(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.baseId = data.getInt("base_item");
        this.extraData = data.getString("extra_data");

        try {
            if (this.getDefinition().getInteraction().equals("gift")) {
                this.giftData = JsonFactory.getInstance().fromJson(this.extraData.split("GIFT::##")[1], GiftData.class);
            }
        } catch (Exception e) {
            this.giftData = null;
        }

        this.limitedEditionItem = LimitedEditionDao.get(this.id);
    }

    public InventoryItem(int id, int baseId, String extraData, GiftData giftData) {
        this.init(id, baseId, extraData, giftData);

        this.limitedEditionItem = LimitedEditionDao.get(this.id);
    }

    public InventoryItem(int id, int baseId, String extraData) {
        this.init(id, baseId, extraData, null);
    }

    private void init(int id, int baseId, String extraData, GiftData giftData) {
        this.id = id;
        this.baseId = baseId;
        this.extraData = extraData;
        this.giftData = giftData;
    }

    public void compose(Composer msg) {
        final boolean isGift = this.getGiftData() != null;
        final boolean isGroupItem = this.getDefinition().getInteraction().equals("group_item") || this.getDefinition().getInteraction().equals("group_gate");
        final boolean isLimited = this.getLimitedEditionItem() != null;

        msg.writeInt(this.getId());
        msg.writeString(this.getDefinition().getType().toUpperCase());
        msg.writeInt(this.getId());
        msg.writeInt(isGift ? this.getGiftData().getSpriteId() : this.getDefinition().getSpriteId());

        if (!isGroupItem)
            msg.writeInt(1);

        if (isGroupItem) {
            // Append the group data...
            int groupId = 0;

            msg.writeInt(17);

            if (StringUtils.isNumeric(this.getExtraData())) {
                groupId = Integer.parseInt(this.getExtraData());
            }

            GroupData groupData = groupId == 0 ? null : GroupManager.getInstance().getData(groupId);

            if (groupData == null) {
                msg.writeInt(0);
            } else {
                msg.writeInt(2);
                msg.writeInt(5);
                msg.writeString("0"); //state
                msg.writeString(groupId);
                msg.writeString(groupData.getBadge());

                String colourA = GroupManager.getInstance().getGroupItems().getSymbolColours().get(groupData.getColourA()) != null ? GroupManager.getInstance().getGroupItems().getSymbolColours().get(groupData.getColourA()).getColour() : "ffffff";
                String colourB = GroupManager.getInstance().getGroupItems().getBackgroundColours().get(groupData.getColourB()) != null ? GroupManager.getInstance().getGroupItems().getBackgroundColours().get(groupData.getColourB()).getColour() : "ffffff";

                msg.writeString(colourA);
                msg.writeString(colourB);
            }
        } else if (isLimited) {
            msg.writeString("");
            msg.writeBoolean(true);
            msg.writeBoolean(false);
        } else if (this.getDefinition().getInteraction().equals("badge_display") && !isGift) {
            msg.writeInt(2);
        } else {
            msg.writeInt(0);
        }

        if (this.getDefinition().getInteraction().equals("badge_display") && !isGift) {
            msg.writeInt(4);

            msg.writeString("0");
            msg.writeString(this.getExtraData());
            msg.writeString(""); // creator
            msg.writeString(""); // date
        } else if (!isGroupItem) {
            msg.writeString(!isGift ? this.getExtraData() : "");
        }

        if (isLimited && !isGift) {
            LimitedEditionItem limitedEditionItem = this.getLimitedEditionItem();

            msg.writeInt(limitedEditionItem.getLimitedRare());
            msg.writeInt(limitedEditionItem.getLimitedRareTotal());
        }

        msg.writeBoolean(this.getDefinition().canRecycle());
        msg.writeBoolean(!isGift && this.getDefinition().canTrade());
        msg.writeBoolean(!isLimited && !isGift && this.getDefinition().canInventoryStack());
        msg.writeBoolean(!isGift && this.getDefinition().canMarket());

        msg.writeInt(-1);
        msg.writeBoolean(true);//??
        msg.writeInt(-1);
        msg.writeString("");
        msg.writeInt(isGift ? this.getGiftData().getWrappingPaper() * 1000 + this.getGiftData().getDecorationType() : 0);
    }

    public void serializeTrade(Composer msg) {
        final boolean isGift = this.getGiftData() != null;
        final boolean isGroupItem = this.getDefinition().getInteraction().equals("group_item") || this.getDefinition().getInteraction().equals("group_gate");
        final boolean isLimited = this.getLimitedEditionItem() != null;

        msg.writeInt(this.id);
        msg.writeString(this.getDefinition().getType().toLowerCase());
        msg.writeInt(this.id);
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeInt(0);
        msg.writeBoolean(true);

        if (isGroupItem) {
            // Append the group data...
            int groupId = 0;

//            msg.writeInt(17);

            if (StringUtils.isNumeric(this.getExtraData())) {
                groupId = Integer.parseInt(this.getExtraData());
            }

            GroupData groupData = groupId == 0 ? null : GroupManager.getInstance().getData(groupId);

            if (groupData == null) {
                msg.writeInt(0);
            } else {
                msg.writeInt(2);
                msg.writeInt(5);
                msg.writeString("0"); //state
                msg.writeString(groupId);
                msg.writeString(groupData.getBadge());

                String colourA = GroupManager.getInstance().getGroupItems().getSymbolColours().get(groupData.getColourA()) != null ? GroupManager.getInstance().getGroupItems().getSymbolColours().get(groupData.getColourA()).getColour() : "ffffff";
                String colourB = GroupManager.getInstance().getGroupItems().getBackgroundColours().get(groupData.getColourB()) != null ? GroupManager.getInstance().getGroupItems().getBackgroundColours().get(groupData.getColourB()).getColour() : "ffffff";

                msg.writeString(colourA);
                msg.writeString(colourB);
            }
        } else if (isLimited) {
            msg.writeString("");
            msg.writeBoolean(true);
            msg.writeBoolean(false);
        } else if (this.getDefinition().getInteraction().equals("badge_display") && !isGift) {
            msg.writeInt(2);
        } else {
            msg.writeInt(0);
        }

        if (this.getDefinition().getInteraction().equals("badge_display") && !isGift) {
            msg.writeInt(4);

            msg.writeString("0");
            msg.writeString(this.getExtraData());
            msg.writeString(""); // creator
            msg.writeString(""); // date
        } else if (!isGroupItem) {
            msg.writeString(!isGift ? this.getExtraData() : "");
        }

        if (isLimited && !isGift) {
            LimitedEditionItem limitedEditionItem = this.getLimitedEditionItem();

            msg.writeInt(limitedEditionItem.getLimitedRare());
            msg.writeInt(limitedEditionItem.getLimitedRareTotal());
        }

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);

        if (this.getDefinition().getType().equals("s")) {
            msg.writeInt(0);
        }
    }

    public int getId() {
        return this.id;
    }

    public ItemDefinition getDefinition() {
        return ItemManager.getInstance().getDefinition(this.getBaseId());
    }

    public int getBaseId() {
        return this.baseId;
    }

    public String getExtraData() {
        return this.extraData;
    }

    public GiftData getGiftData() {
        return giftData;
    }

    public LimitedEditionItem getLimitedEditionItem() {
        return limitedEditionItem;
    }
}
