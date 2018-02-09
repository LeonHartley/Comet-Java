package com.cometproject.server.game.players.components.types.inventory.rewrite.items;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.players.components.types.inventory.rewrite.InventoryItem;
import com.cometproject.server.utilities.JsonUtil;

public class GiftInventoryItem extends InventoryItem {

    private final GiftData giftData;

    public GiftInventoryItem(long id, int virtualId, String data, FurnitureDefinition furnitureDefinition, LimitedEditionItem limitedEditionItem) {
        super(id, virtualId, data, furnitureDefinition, limitedEditionItem);

        this.giftData = this.getGiftData(data);
    }

    private GiftData getGiftData(String data) {

        try {
            if (this.getFurnitureDefinition().getInteraction().equals("gift")) {
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
