package com.cometproject.storage.mysql.repositories;

import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.players.data.components.inventory.IPlayerItemFactory;
import com.cometproject.api.game.players.data.components.inventory.InventoryItemData;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.api.game.rooms.objects.data.LimitedEditionItemData;
import com.cometproject.storage.api.repositories.IInventoryRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.data.results.IResultReader;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;

public class MySQLInventoryRepository extends MySQLRepository implements IInventoryRepository {
    private final IPlayerItemFactory playerItemFactory;

    public MySQLInventoryRepository(IPlayerItemFactory playerItemFactory, MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);

        this.playerItemFactory = playerItemFactory;
    }

    @Override
    public void getInventoryByPlayerId(int playerId, Consumer<List<PlayerItem>> itemConsumer) {
        final List<PlayerItem> items = Lists.newArrayList();

        select("SELECT i.*, ltd.limited_id, ltd.limited_total FROM items i LEFT JOIN items_limited_edition ltd ON ltd.item_id = i.id WHERE room_id = 0 AND user_id = ? ORDER by id DESC;", (data) -> {
            items.add(this.buildItem(data));
        }, playerId);

        itemConsumer.accept(items);
    }

    private PlayerItem buildItem(IResultReader data) throws Exception {
        final long id = data.readLong("id");
        final int baseId = data.readInteger("base_item");
        final String extra_data = data.readString("extra_data");

        LimitedEditionItem limitedEditionItemData = null;

        if (data.readInteger("limited_id") != 0) {
            limitedEditionItemData = new LimitedEditionItemData(data.readLong("id"),
                    data.readInteger("limited_id"), data.readInteger("limited_total"));
        }

        return this.playerItemFactory.createItem(new InventoryItemData(id, baseId, extra_data, limitedEditionItemData));
    }
}
