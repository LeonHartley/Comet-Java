package com.cometproject.server.game.items.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDefinition {
    private int id;
    private String publicName;
    private String itemName;
    private String type;
    private int width;
    private int length;
    private double height;
    private int spriteId;

    public boolean canStack;
    public boolean canSit;
    public boolean canWalk;
    public boolean canTrade;
    public boolean canRecycle;
    public boolean canMarket;
    public boolean canGift;
    public boolean canInventoryStack;

    private int effectId;
    private String interaction;
    private int interactionCycleCount;
    private String[] vendingIds;

    public ItemDefinition(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.publicName = data.getString("public_name");
        this.itemName = data.getString("item_name");
        this.type = data.getString("type");
        this.width = data.getInt("width");
        this.length = data.getInt("length");
        this.height = data.getDouble("stack_height");
        this.spriteId = data.getInt("sprite_id");

        this.canStack = data.getString("can_stack").equals("1");
        this.canSit = data.getString("can_sit").equals("1");
        this.canWalk = data.getString("is_walkable").equals("1");
        this.canTrade = data.getString("allow_trade").equals("1");
        this.canInventoryStack = data.getString("allow_inventory_stack").equals("1");

        this.canRecycle = true;
        this.canMarket = true;
        this.canGift = true;

        this.effectId = data.getInt("effectid");
        this.interaction = data.getString("interaction_type");
        this.interactionCycleCount = data.getInt("interaction_modes_count");
        this.vendingIds = data.getString("vending_ids").isEmpty() ? new String[0] : data.getString("vending_ids").split(",");
    }

    public boolean isAdFurni() {
        return itemName.equals("ads_mpu_720") || this.itemName.equals("ads_background") || this.itemName.equals("ads_mpu_300") || this.itemName.equals("ads_mpu_160");
    }

    public boolean isRoomDecor() {
        return itemName.startsWith("wallpaper") || itemName.startsWith("landscape") || itemName.startsWith("a2 ");
    }

    public int getId() {
        return this.id;
    }

    public String getPublicName() {
        return this.publicName;
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getType() {
        return this.type;
    }

    public int getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public int getSpriteId() {
        return spriteId;
    }

    public int getLength() {
        return length;
    }

    public String getInteraction() {
        return interaction;
    }

    public int getInteractionCycleCount() {
        return this.interactionCycleCount;
    }

    public int getEffectId() {
        return effectId;
    }

    public String[] getVendingIds() {
        return vendingIds;
    }
}
