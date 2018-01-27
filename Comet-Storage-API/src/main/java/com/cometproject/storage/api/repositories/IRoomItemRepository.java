package com.cometproject.storage.api.repositories;

import com.cometproject.api.game.rooms.objects.IRoomItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;

import java.util.List;
import java.util.function.Consumer;

public interface IRoomItemRepository {

    void getItemsByRoomId(final int roomId, Consumer<List<RoomItemData>> itemConsumer);

}
