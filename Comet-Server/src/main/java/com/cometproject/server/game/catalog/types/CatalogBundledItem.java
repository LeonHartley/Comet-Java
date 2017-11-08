package com.cometproject.server.game.catalog.types;

import com.cometproject.api.game.catalog.types.ICatalogBundledItem;

public class CatalogBundledItem implements ICatalogBundledItem {

    private final int itemId;
    private final int amount;
    private final String presetData;

    public CatalogBundledItem(String presetData, int amount, int itemId) {
        this.presetData = presetData;
        this.amount = amount;
        this.itemId = itemId;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public String getPresetData() {
        return presetData;
    }

}