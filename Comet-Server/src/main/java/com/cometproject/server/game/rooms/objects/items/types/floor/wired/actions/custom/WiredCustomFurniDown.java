package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.types.Room;

public class WiredCustomFurniDown extends WiredCustomFurniUp {

    public WiredCustomFurniDown(RoomItemData itemData, Room room) {
        super(itemData, room);
        this.isUp = false;
    }
}
