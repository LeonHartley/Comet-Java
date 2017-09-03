package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class WiredAddonBlob extends RoomItemFloor {

    public WiredAddonBlob(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);

        this.setExtraData("0");
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        // award point
    }
}
