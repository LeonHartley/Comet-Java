package com.cometproject.server.game.rooms.items.types;

import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.rooms.items.RoomItemWall;

public final class GenericWallItem extends RoomItemWall {
    public GenericWallItem(int id, int itemId, int roomId, int owner, String position, String data) {
        super(id, itemId, roomId, owner, position, data);
    }
}
