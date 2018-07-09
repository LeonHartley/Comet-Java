package com.cometproject.storage.api.repositories;

import com.cometproject.api.game.catalog.types.purchase.CatalogPurchase;
import com.cometproject.api.game.rooms.objects.IRoomItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface IRoomItemRepository {

    void getItemsByRoomId(final int roomId, Consumer<List<RoomItemData>> itemConsumer);

    void removeItemFromRoom(final long itemId, final int playerId, final String finalState);

    void deleteItem(long itemId);

    void saveData(long itemId, String data);

    void getRoomIdByItemId(long itemId, Consumer<Integer> idConsumer);

    void saveItemPosition(int x, int y, double height, int rotation, long id);

    void placeFloorItem(long roomId, int x, int y, double height, int rot, String data, int baseItem, long itemId);

    void placeWallItem(int roomId, String wallPosition, String data, long itemId);

    void placeBundle(int roomId, final Set<IRoomItemData> bundle);

    void saveItem(IRoomItemData itemData);

    void createItem(int playerId, int itemId, String data, Consumer<Long> idConsumer);

    void purchaseItems(List<CatalogPurchase> purchases, Consumer<List<Long>> idConsumer, int viewingUser);

    void saveItemBatch(final Set<IRoomItemData> data);

    void setBaseItem(long itemId, int baseId);

    void saveReward(long itemId, int playerId, String rewardData);

    void getGivenRewards(long id, Consumer<Map<Integer, Set<String>>> rewardsConsumer);
}
