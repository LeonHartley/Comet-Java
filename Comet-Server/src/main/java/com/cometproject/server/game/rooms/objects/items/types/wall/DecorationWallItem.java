package com.cometproject.server.game.rooms.objects.items.types.wall;

import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.RoomInstance;


public final class DecorationWallItem extends RoomItemWall {
    public DecorationWallItem(int id, int itemId, RoomInstance room, int owner, String position, String data) {
        super(id, itemId, room, owner, position, data);
    }
}
