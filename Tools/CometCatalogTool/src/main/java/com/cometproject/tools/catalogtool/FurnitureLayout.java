package com.cometproject.tools.catalogtool;

import com.cometproject.api.networking.messages.IMessageComposer;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by Matty on 06/05/2014.
 */
public class FurnitureLayout {
    private int id;
    private String publicName;
    private String itemName;
    private String type;
    private int width;
    private int length;
    private double height;
    private int spriteId;

    public String canStack;
    public String canSit;
    public String canWalk;
    private String allowRecycle;
    private String allowMarketplaceSell;
    private String allowGift;
    public String canTrade;
    public String canInventoryStack;

    private int effectId;
    private String interaction;
    private int interactionCycleCount;
    private String vendingIds;

    private String isArrow;
    private String footFigure;
    private String stackMultiplier;
    private String subscriber;
    private String heightAdjustable;

    private int revision;

    public FurnitureLayout(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.publicName = data.getString("public_name");
        this.itemName = data.getString("item_name");
        this.type = data.getString("type");
        this.width = data.getInt("width");
        this.length = data.getInt("length");
        this.height = data.getDouble("stack_height");
        this.spriteId = data.getInt("sprite_id");

        this.canStack = data.getString("can_stack");
        this.canSit = data.getString("can_sit");
        this.canWalk = data.getString("is_walkable");
        this.allowRecycle = data.getString("allow_recycle");
        this.allowMarketplaceSell = data.getString("allow_marketplace_sell");
        this.allowGift = data.getString("allow_gift");
        this.canTrade = data.getString("allow_trade");
        this.canInventoryStack = data.getString("allow_inventory_stack");

        this.effectId = data.getInt("effectid");
        this.interaction = data.getString("interaction_type");
        this.interactionCycleCount = data.getInt("interaction_modes_count");
        this.vendingIds = data.getString("vending_ids");

        this.isArrow = data.getString("is_arrow");
        this.footFigure = data.getString("foot_figure");
        this.stackMultiplier = data.getString("stack_multiplier");
        this.subscriber = data.getString("subscriber");
        this.heightAdjustable = data.getString("height_adjustable");

        this.revision = data.getInt("revision");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getSpriteId() {
        return spriteId;
    }

    public void setSpriteId(int spriteId) {
        this.spriteId = spriteId;
    }

    public String getCanStack() {
        return canStack;
    }

    public void setCanStack(String canStack) {
        this.canStack = canStack;
    }

    public String getCanSit() {
        if (canSit.equals("")) {
            return "0";
        }

        return canSit;
    }

    public void setCanSit(String canSit) {
        this.canSit = canSit;
    }

    public String getCanWalk() {
        if (canWalk.equals("")) {
            return "0";
        }

        return canWalk;
    }

    public void setCanWalk(String canWalk) {
        this.canWalk = canWalk;
    }

    public String getCanTrade() {
        if (canTrade.equals("")) {
            return "0";
        }

        return canTrade;
    }

    public void setCanTrade(String canTrade) {
        this.canTrade = canTrade;
    }

    public String getCanInventoryStack() {
        if (canInventoryStack.equals("")) {
            return "0";
        }

        return canInventoryStack;
    }

    public void setCanInventoryStack(String canInventoryStack) {
        this.canInventoryStack = canInventoryStack;
    }

    public int getEffectId() {
        return effectId;
    }

    public void setEffectId(int effectId) {
        this.effectId = effectId;
    }

    public String getInteraction() {
        if (interaction.equals("")) {
            return "default";
        }

        return interaction;
    }

    public void setInteraction(String interaction) {
        this.interaction = interaction;
    }

    public int getInteractionCycleCount() {
        if (interaction.equals("") || interaction.equals("default")) {
            return 0;
        }

        return interactionCycleCount;
    }

    public void setInteractionCycleCount(int interactionCycleCount) {
        this.interactionCycleCount = interactionCycleCount;
    }

    public String getVendingIds() {
        if (vendingIds.equals("")) {
            return "0";
        }

        return vendingIds;
    }

    public void setVendingIds(String vendingIds) {
        this.vendingIds = vendingIds;
    }

    public String getAllowRecycle() {
        if (allowRecycle.equals("")) {
            return "0";
        }

        return allowRecycle;
    }

    public void setAllowRecycle(String allowRecycle) {
        this.allowRecycle = allowRecycle;
    }

    public String getAllowMarketplaceSell() {
        if (allowMarketplaceSell.equals("")) {
            return "0";
        }

        return allowMarketplaceSell;
    }

    public void setAllowMarketplaceSell(String allowMarketplaceSell) {
        this.allowMarketplaceSell = allowMarketplaceSell;
    }

    public String getAllowGift() {
        if (allowGift.equals("")) {
            return "0";
        }

        return allowGift;
    }

    public void setAllowGift(String allowGift) {
        this.allowGift = allowGift;
    }

    public String getIsArrow() {
        if (isArrow.equals("")) {
            return "0";
        }

        return isArrow;
    }

    public void setIsArrow(String isArrow) {
        this.isArrow = isArrow;
    }

    public String getFootFigure() {
        if (footFigure.equals("")) {
            return "0";
        }

        return footFigure;
    }

    public void setFootFigure(String footFigure) {
        this.footFigure = footFigure;
    }

    public String getStackMultiplier() {
        if (stackMultiplier.equals("")) {
            return "0";
        }

        return stackMultiplier;
    }

    public void setStackMultiplier(String stackMultiplier) {
        this.stackMultiplier = stackMultiplier;
    }

    public String getSubscriber() {
        if (subscriber.equals("")) {
            return "0";
        }

        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public String getHeightAdjustable() {
        if (heightAdjustable.equals("")) {
            return "0";
        }

        return heightAdjustable;
    }

    public void setHeightAdjustable(String heightAdjustable) {
        this.heightAdjustable = heightAdjustable;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }
}
