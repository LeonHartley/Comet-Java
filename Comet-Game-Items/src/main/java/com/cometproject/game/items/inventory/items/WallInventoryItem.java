package com.cometproject.game.items.inventory.items;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.players.data.components.inventory.InventoryItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.game.items.inventory.InventoryItem;

public class WallInventoryItem extends InventoryItem {
    public WallInventoryItem(InventoryItemData inventoryItemData, FurnitureDefinition furnitureDefinition) {
        super(inventoryItemData, furnitureDefinition);
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.getVirtualId());
        msg.writeString(this.getDefinition().getType().toUpperCase());
        msg.writeInt(this.getVirtualId());
        msg.writeInt(this.getDefinition().getSpriteId());

        if (this.getDefinition().getItemName().contains("a2")) {
            msg.writeInt(3);
        } else if (this.getDefinition().getItemName().contains("wallpaper")) {
            msg.writeInt(2);
        } else if (this.getDefinition().getItemName().contains("landscape")) {
            msg.writeInt(4);
        } else {
            msg.writeInt(1);
        }

        msg.writeInt(0);
        msg.writeString(this.getExtraData());

        msg.writeBoolean(this.getDefinition().canRecycle());
        msg.writeBoolean(this.getDefinition().canTrade());
        msg.writeBoolean(this.getDefinition().canInventoryStack());
        msg.writeBoolean(this.getDefinition().canMarket());
        msg.writeInt(-1);
        msg.writeBoolean(false);
        msg.writeInt(-1);
    }
}
