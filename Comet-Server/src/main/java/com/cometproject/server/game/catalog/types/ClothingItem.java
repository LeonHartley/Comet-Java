package com.cometproject.server.game.catalog.types;

import com.cometproject.api.game.catalog.types.IClothingItem;

public class ClothingItem implements IClothingItem {
    private final String itemName;

    private final int[] parts;

    public ClothingItem(final String itemName, final int[] parts) {
        this.itemName = itemName;
        this.parts = parts;
    }

    public String getItemName() {
        return itemName;
    }

    public int[] getParts() {
        return parts;
    }
}
