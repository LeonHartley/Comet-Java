package com.cometproject.server.game.players.components.types.inventory.rewrite;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.networking.messages.IComposer;

public abstract class InventoryItem {

    private final long id;
    private final int virtualId;
    private final String data;
    private final FurnitureDefinition furnitureDefinition;
    private final LimitedEditionItem limitedEditionItem;

    public InventoryItem(long id, int virtualId, String data, FurnitureDefinition furnitureDefinition,
                         LimitedEditionItem limitedEditionItem) {
        this.id = id;
        this.virtualId = virtualId;
        this.data = data;
        this.furnitureDefinition = furnitureDefinition;
        this.limitedEditionItem = limitedEditionItem;
    }

    protected boolean composeData(IComposer msg) {
        msg.writeInt(1);

        if(this.limitedEditionItem != null) {
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
        msg.writeString(this.getFurnitureDefinition().getType());
        msg.writeInt(this.getVirtualId());
        msg.writeInt(this.getSpriteId());

        if(!this.composeData(msg)) {
            msg.writeString(this.getExtraData());
        }

        if(this.limitedEditionItem != null) {
            msg.writeInt(this.limitedEditionItem.getLimitedRare());
            msg.writeInt(this.limitedEditionItem.getLimitedRareTotal());
        }

        msg.writeBoolean(this.canRecycle());
        msg.writeBoolean(this.canTrade());
        msg.writeBoolean(this.limitedEditionItem == null && this.canInventoryStack());
        msg.writeBoolean(this.canMarketplace());

        msg.writeInt(-1);
        msg.writeBoolean(true);
        msg.writeInt(-1);
        msg.writeString("");
        msg.writeInt(this.getExtraInt());
    }

    protected boolean canTrade() {
        return this.furnitureDefinition.canTrade();
    }

    protected boolean canRecycle() {
        return this.furnitureDefinition.canRecycle();
    }

    protected boolean canInventoryStack() {
        return this.furnitureDefinition.canInventoryStack();
    }

    protected boolean canMarketplace() {
        return this.furnitureDefinition.canMarket();
    }

    protected int getExtraInt() {
        return 0;
    }

    protected int getSpriteId() {
        return this.furnitureDefinition.getSpriteId();
    }

    public FurnitureDefinition getFurnitureDefinition() {
        return this.furnitureDefinition;
    }

    public int getVirtualId() {
        return this.virtualId;
    }

    public String getExtraData() {
        return this.data;
    }
}