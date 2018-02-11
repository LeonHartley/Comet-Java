package com.cometproject.game.items.inventory.items;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.players.data.components.inventory.InventoryItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.game.items.inventory.InventoryItem;

public class WiredInventoryItem extends InventoryItem {
    public WiredInventoryItem(InventoryItemData inventoryItemData, FurnitureDefinition furnitureDefinition) {
        super(inventoryItemData, furnitureDefinition);
    }

    @Override
    public boolean composeData(IComposer msg) {
        super.composeData(msg);

        msg.writeString("");
        return true;
    }

}
