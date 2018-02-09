package com.cometproject.server.game.players.components.types.inventory.rewrite.items;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.components.types.inventory.rewrite.InventoryItem;

public class WiredInventoryItem extends InventoryItem {
    public WiredInventoryItem(long id, int virtualId, String data, FurnitureDefinition furnitureDefinition, LimitedEditionItem limitedEditionItem) {
        super(id, virtualId, data, furnitureDefinition, limitedEditionItem);
    }

    @Override
    public boolean composeData(IComposer msg) {
        super.composeData(msg);

        msg.writeString("");
        return true;
    }

}
