package com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;

public class FreezeTileFloorItem extends RoomItemFloor {
    public FreezeTileFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if(!(entity instanceof PlayerEntity)) {
            return false;
        }

        if(isWiredTrigger) {
            return false;
        }

        if(entity.getTile() != this.getTile()) {
            return false;
        }

        
        return true;
    }
}
