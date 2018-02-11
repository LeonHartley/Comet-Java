package com.cometproject.game.items.inventory.items;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.GiftData;
import com.cometproject.api.game.players.data.components.inventory.InventoryItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.game.items.inventory.InventoryItem;

public class GiftInventoryItem extends InventoryItem {

    private final GiftData giftData;

    public GiftInventoryItem(InventoryItemData inventoryItemData, FurnitureDefinition furnitureDefinition) {
        super(inventoryItemData, furnitureDefinition);

        this.giftData = this.getGiftData(inventoryItemData.getExtraData());
    }

    private GiftData getGiftData(String data) {

        try {
            if (this.getDefinition().getInteraction().equals("gift")) {
                return JsonUtil.getInstance().fromJson(data.split("GIFT::##")[1], GiftData.class);
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean composeData(IComposer msg) {
        super.composeData(msg);

        msg.writeString("");
        return true;
    }

    @Override
    public int getExtraInt() {
        if(this.giftData == null) {
            return 0;
        }

        return this.giftData.getWrappingPaper() * 1000 + this.giftData.getDecorationType();
    }

}
