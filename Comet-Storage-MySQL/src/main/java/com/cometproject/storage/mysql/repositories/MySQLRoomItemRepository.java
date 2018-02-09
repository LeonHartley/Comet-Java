package com.cometproject.storage.mysql.repositories;

import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.rooms.objects.IRoomItemData;
import com.cometproject.api.game.rooms.objects.data.LimitedEditionItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.storage.api.repositories.IRoomItemRepository;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.data.results.IResultReader;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;

public class MySQLRoomItemRepository extends MySQLRepository implements IRoomItemRepository {
    public MySQLRoomItemRepository(MySQLConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public void getItemsByRoomId(int roomId, Consumer<List<RoomItemData>> itemConsumer) {
        final List<RoomItemData> itemData = Lists.newArrayList();

        select("SELECT i.*, player.username AS user_name, ltd.limited_id, ltd.limited_total FROM items i " +
                        "LEFT JOIN items_limited_edition ltd ON ltd.item_id = i.id " +
                        "RIGHT JOIN players player ON player.id = i.user_id WHERE i.room_id = ?;",
                data -> itemData.add(buildItem(data)), roomId);

        itemConsumer.accept(itemData);
    }

    private RoomItemData buildItem(IResultReader data) throws Exception {
        LimitedEditionItem limitedEditionItemData = null;

        if (data.readInteger("limited_id") != 0) {
            limitedEditionItemData = new LimitedEditionItemData(data.readLong("id"),
                    data.readInteger("limited_id"), data.readInteger("limited_total"));
        }

        final long id = data.readLong("id");
        final int itemId = data.readInteger("base_item");
        final int ownerId = data.readInteger("user_id");
        final String ownerName = data.readString("user_name");
        final int x = data.readInteger("x");
        final int y = data.readInteger("y");
        final double z = data.readDouble("z");
        final int rotation = data.readInteger("rot");
        final String extraData = data.readString("extra_data");
        final String wallPosition = data.readString("wall_pos");

        return new RoomItemData(id, itemId, ownerId, ownerName, new Position(x, y, z), rotation, extraData, wallPosition, limitedEditionItemData);
    }
}
