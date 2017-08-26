package com.cometproject.server.game.catalog.types;

public class ClothingItem {
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
