package com.cometproject.api.game.players.data.components.inventory;

public interface IInventoryItemSnapshot {
   long getId();

   int getBaseItemId();

   String getExtraData();
}
