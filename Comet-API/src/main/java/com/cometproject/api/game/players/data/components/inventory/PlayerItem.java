package com.cometproject.api.game.players.data.components.inventory;

import com.cometproject.api.game.furniture.types.IFurnitureDefinition;
import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.networking.messages.IComposer;

public interface PlayerItem {
    long getId();

    IFurnitureDefinition getDefinition();

    int getBaseId();

    String getExtraData();

    LimitedEditionItem getLimitedEditionItem();

    int getVirtualId();

    void compose(IComposer message);

    PlayerItemSnapshot createSnapshot();
}

