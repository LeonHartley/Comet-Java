package com.cometproject.api.game.players.data.components.inventory;

import com.cometproject.api.game.furniture.types.IGiftData;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;

public final class InventoryItemData {

    private final long id;
    private final int baseId;
    private final String extraData;
    private final LimitedEditionItem limitedEditionItem;

    public InventoryItemData(long id, int baseId, String extraData, LimitedEditionItem limitedEditionItem) {
        this.id = id;
        this.baseId = baseId;
        this.extraData = extraData;
        this.limitedEditionItem = limitedEditionItem;
    }

    public long getId() {
        return id;
    }

    public int getBaseId() {
        return baseId;
    }

    public String getExtraData() {
        return extraData;
    }

    public LimitedEditionItem getLimitedEditionItem() {
        return limitedEditionItem;
    }
}
