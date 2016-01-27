package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.components.InventoryComponent;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Map;


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

        for (Map.Entry<Long, InventoryItem>  inventoryItem : inventoryComponent.getFloorItems().entrySet()) {
            inventoryItem.getValue().compose(msg);
        }

        // Wall items
        for (Map.Entry<Long, InventoryItem> item : inventoryComponent.getWallItems().entrySet()) {
            msg.writeInt(item.getValue().getVirtualId());
            msg.writeString(item.getValue().getDefinition().getType().toUpperCase());
            msg.writeInt(item.getValue().getVirtualId());
            msg.writeInt(item.getValue().getDefinition().getSpriteId());

            if (item.getValue().getDefinition().getItemName().contains("a2")) {
                msg.writeInt(3);
            } else if (item.getValue().getDefinition().getItemName().contains("wallpaper")) {
                msg.writeInt(2);
            } else if (item.getValue().getDefinition().getItemName().contains("landscape")) {
                msg.writeInt(4);
            } else {
                msg.writeInt(1);
            }

            msg.writeInt(0);
            msg.writeString(item.getValue().getExtraData());

            msg.writeBoolean(item.getValue().getDefinition().canRecycle());
            msg.writeBoolean(item.getValue().getDefinition().canTrade());
            msg.writeBoolean(item.getValue().getDefinition().canInventoryStack());
            msg.writeBoolean(item.getValue().getDefinition().canMarket());
            msg.writeInt(-1);
            msg.writeBoolean(false);
            msg.writeInt(-1);
        }
    }
}
