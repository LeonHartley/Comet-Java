package com.cometproject.storage.api.repositories;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;

import java.util.List;
import java.util.function.Consumer;

public interface IInventoryRepository {

    void getInventoryByPlayerId(int playerId, Consumer<List<PlayerItem>> itemConsumer);

}
