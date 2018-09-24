package com.cometproject.game.items.inventory;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.players.data.components.inventory.InventoryItemData;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.api.game.players.data.components.inventory.PlayerItemSnapshot;
import com.cometproject.api.networking.messages.IComposer;
import com.google.gson.JsonObject;

public class InventoryItem implements PlayerItem {

    private final InventoryItemData itemData;
    private final FurnitureDefinition furnitureDefinition;

    public InventoryItem(InventoryItemData itemData, FurnitureDefinition furnitureDefinition) {
        this.itemData = itemData;
        this.furnitureDefinition = furnitureDefinition;
    }

    protected boolean composeData(IComposer msg) {
        msg.writeInt(1);

        if(this.itemData.getLimitedEditionItem() != null) {
            msg.writeString("");
            msg.writeBoolean(true);
            msg.writeBoolean(false);
        } else {
            msg.writeInt(0);
        }

        return false;
    }

    public void compose(IComposer msg) {
        msg.writeInt(this.getVirtualId());
        msg.writeString(this.getDefinition().getType());
        msg.writeInt(this.getVirtualId());
        msg.writeInt(this.getSpriteId());

        if(!this.composeData(msg)) {
            msg.writeString(this.getExtraData());
        }

        if(this.itemData.getLimitedEditionItem() != null) {
            msg.writeInt(this.itemData.getLimitedEditionItem().getLimitedRare());
            msg.writeInt(this.itemData.getLimitedEditionItem().getLimitedRareTotal());
        }

        msg.writeBoolean(this.canRecycle());
        msg.writeBoolean(this.canTrade());
        msg.writeBoolean(this.itemData.getLimitedEditionItem() == null && this.canInventoryStack());
        msg.writeBoolean(this.canMarketplace());

        msg.writeInt(-1);
        msg.writeBoolean(true);
        msg.writeInt(-1);
        msg.writeString("");
        msg.writeInt(this.getExtraInt());
    }

    @Override
    public PlayerItemSnapshot createSnapshot() {
        return null;
    }

    private boolean canTrade() {
        return this.furnitureDefinition.canTrade();
    }

    private boolean canRecycle() {
        return this.furnitureDefinition.canRecycle();
    }

    private boolean canInventoryStack() {
        return this.furnitureDefinition.canInventoryStack();
    }

    private boolean canMarketplace() {
        return this.furnitureDefinition.canMarket();
    }

    protected int getExtraInt() {
        return 0;
    }

    protected int getSpriteId() {
        return this.furnitureDefinition.getSpriteId();
    }

    @Override
    public long getId() {
        return this.itemData.getId();
    }

    @Override
    public FurnitureDefinition getDefinition() {
        return this.furnitureDefinition;
    }

    @Override
    public int getBaseId() {
        return this.itemData.getBaseId();
    }

    @Override
    public String getExtraData() {
        return this.itemData.getExtraData();
    }

    @Override
    public LimitedEditionItem getLimitedEditionItem() {
        return this.itemData.getLimitedEditionItem();
    }

    @Override
    public int getVirtualId() {
        return GameContext.getCurrent().getFurnitureService().getItemVirtualId(this.itemData.getId());
    }
}