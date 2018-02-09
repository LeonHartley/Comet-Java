package com.cometproject.server.game.players.components.types.inventory.rewrite.items;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.components.types.inventory.rewrite.InventoryItem;

public class WallInventoryItem extends InventoryItem {
    public WallInventoryItem(long id, int virtualId, String data, FurnitureDefinition furnitureDefinition, LimitedEditionItem limitedEditionItem) {
        super(id, virtualId, data, furnitureDefinition, limitedEditionItem);
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.getVirtualId());
        msg.writeString(this.getFurnitureDefinition().getType().toUpperCase());
        msg.writeInt(this.getVirtualId());
        msg.writeInt(this.getFurnitureDefinition().getSpriteId());

        if (this.getFurnitureDefinition().getItemName().contains("a2")) {
            msg.writeInt(3);
        } else if (this.getFurnitureDefinition().getItemName().contains("wallpaper")) {
            msg.writeInt(2);
        } else if (this.getFurnitureDefinition().getItemName().contains("landscape")) {
            msg.writeInt(4);
        } else {
            msg.writeInt(1);
        }

        msg.writeInt(0);
        msg.writeString(this.getExtraData());

        msg.writeBoolean(this.getFurnitureDefinition().canRecycle());
        msg.writeBoolean(this.getFurnitureDefinition().canTrade());
        msg.writeBoolean(this.getFurnitureDefinition().canInventoryStack());
        msg.writeBoolean(this.getFurnitureDefinition().canMarket());
        msg.writeInt(-1);
        msg.writeBoolean(false);
        msg.writeInt(-1);
    }
}
