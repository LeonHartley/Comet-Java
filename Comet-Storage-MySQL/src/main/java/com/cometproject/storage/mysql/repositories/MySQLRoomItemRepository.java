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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

    @Override
    public void removeItemFromRoom(long itemId, int playerId, String finalState) {
        update("UPDATE items SET room_id = 0, user_id = ?, x = 0, " +
                "y = 0, z = 0, wall_pos = '', extra_data = ? WHERE id = ?", playerId, finalState, itemId);
    }

    @Override
    public void deleteItem(long itemId) {
        update("DELETE FROM items WHERE id = ?;", itemId);
    }

    @Override
    public void saveData(long itemId, String data) {
        update("UPDATE items SET extra_data = ? WHERE id = ?;", data, itemId);
    }

    @Override
    public void getRoomIdByItemId(long itemId, Consumer<Integer> idConsumer) {
        select("SELECT `room_id` FROM items WHERE id = ? LIMIT 1;", (data) -> {
            idConsumer.accept(data.readInteger("room_id"));
        }, itemId);
    }

    @Override
    public void saveItemPosition(int x, int y, double height, int rotation, long id) {
        update("UPDATE `items` SET x = ?, y = ?, z = ?, rot = ? WHERE id = ?;", x, y, height, rotation, id);
    }

    @Override
    public void placeFloorItem(long roomId, int x, int y, double height, int rot, String data, long itemId) {
        update("UPDATE items SET x = ?, y = ?, z = ?, rot = ?, room_id = ?, extra_data = ? WHERE id = ?;",
                x, y, height, rot, roomId, data, itemId);
    }

    @Override
    public void placeWallItem(int roomId, String wallPosition, String data, long itemId) {
        update("UPDATE items SET room_id = ?, wall_pos = ?, extra_data = ? WHERE id = ?;",
                roomId, wallPosition, itemId);
    }

    @Override
    public void setBaseItem(long itemId, int baseId) {
        update("UPDATE items SET base_item = ? WHERE id = ?;", baseId, itemId);
    }

    @Override
    public void saveReward(long itemId, int playerId, String rewardData) {
        update("INSERT into items_wired_rewards (item_id, player_id, reward_data) VALUES(?, ?, ?);",
                itemId, playerId, rewardData);
    }

    @Override
    public void getGivenRewards(long id, Consumer<Map<Integer, Set<String>>> rewardsConsumer) {
        Map<Integer, Set<String>> data = new ConcurrentHashMap<>();

        select("SELECT player_id, reward_data FROM items_wired_rewards WHERE item_id = ?;", (resultReader) -> {
            final int playerId = resultReader.readInteger("player_id");
            final String rewardData = resultReader.readString("reward_data");

            if (!data.containsKey(playerId)) {
                data.put(playerId, new HashSet<>());
            }

            data.get(playerId).add(rewardData);
        }, id);

        rewardsConsumer.accept(data);
    }

    @Override
    public void saveItem(IRoomItemData roomItemData) {
        update("UPDATE items SET x = ?, y = ?, z = ?, rot = ?, extra_data = ? WHERE id = ?;",
                roomItemData.getPosition().getX(), roomItemData.getPosition().getY(),
                roomItemData.getPosition().getZ(), roomItemData.getRotation(),
                roomItemData.getData(), roomItemData.getId());
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
