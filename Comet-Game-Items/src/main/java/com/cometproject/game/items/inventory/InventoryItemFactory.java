package com.cometproject.game.items.inventory;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.furniture.IFurnitureService;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.ItemType;
import com.cometproject.api.game.players.data.components.inventory.IPlayerItemFactory;
import com.cometproject.api.game.players.data.components.inventory.InventoryItemData;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.game.items.inventory.items.*;

public class InventoryItemFactory implements IPlayerItemFactory {
    private IFurnitureService furnitureService;

    public InventoryItemFactory() {
    }

    @Override
    public PlayerItem createItem(InventoryItemData itemData) {
        if (this.furnitureService == null) {
            this.furnitureService = GameContext.getCurrent().getFurnitureService();
        }

        final FurnitureDefinition itemDefinition = this.furnitureService.getDefinition(itemData.getBaseId());

        if (itemDefinition.isWired()) {
            return new WiredInventoryItem(itemData, itemDefinition);

        } else if (itemDefinition.getInteraction().startsWith("group_")) {
            return new GroupInventoryItem(itemData, itemDefinition);

        } else if (itemDefinition.getInteraction().equals("gift")) {
            return new GiftInventoryItem(itemData, itemDefinition);

        } else if (itemDefinition.getItemType() == ItemType.WALL) {
            return new WallInventoryItem(itemData, itemDefinition);

        } else if (itemDefinition.getInteraction().equals("badge_display")) {
            return new BadgeDisplayInventoryItem(itemData, itemDefinition);
        }

        return new InventoryItem(itemData, itemDefinition);
    }
}
