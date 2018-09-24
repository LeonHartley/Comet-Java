package com.cometproject.api.game.players.data.components.inventory;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.networking.messages.IComposer;
import com.google.gson.JsonObject;

public interface PlayerItem {
    long getId();

    FurnitureDefinition getDefinition();

    int getBaseId();

    String getExtraData();

    LimitedEditionItem getLimitedEditionItem();

    int getVirtualId();

    void compose(IComposer message);

    PlayerItemSnapshot createSnapshot();
}

