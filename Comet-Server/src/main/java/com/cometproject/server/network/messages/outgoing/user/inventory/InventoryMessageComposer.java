package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.components.InventoryComponent;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class InventoryMessageComposer extends MessageComposer {
    private final InventoryComponent inventoryComponent;

    public InventoryMessageComposer(final InventoryComponent inventoryComponent) {
        this.inventoryComponent = inventoryComponent;
    }

    @Override
    public short getId() {
        return Composers.FurniListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(1);
        msg.writeInt(1);
        msg.writeInt(inventoryComponent.getTotalSize());

        for (InventoryItem inventoryItem : inventoryComponent.getFloorItems().values()) {
            inventoryItem.compose(msg);
        }

        // Wall items
        for (InventoryItem i : inventoryComponent.getWallItems().values()) {
            msg.writeInt(i.getId());
            msg.writeString(i.getDefinition().getType().toUpperCase());
            msg.writeInt(i.getId());
            msg.writeInt(i.getDefinition().getSpriteId());

            if (i.getDefinition().getItemName().contains("a2")) {
                msg.writeInt(3);
            } else if (i.getDefinition().getItemName().contains("wallpaper")) {
                msg.writeInt(2);
            } else if (i.getDefinition().getItemName().contains("landscape")) {
                msg.writeInt(4);
            } else {
                msg.writeInt(1);
            }

            msg.writeInt(0);
            msg.writeString(i.getExtraData());

            msg.writeBoolean(i.getDefinition().canRecycle());
            msg.writeBoolean(i.getDefinition().canTrade());
            msg.writeBoolean(i.getDefinition().canInventoryStack());
            msg.writeBoolean(i.getDefinition().canMarket());
            msg.writeInt(-1);
            msg.writeBoolean(false);
            msg.writeInt(-1);
        }
    }
}
