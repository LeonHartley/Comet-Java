package com.cometproject.server.game.rooms.objects.items.types;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;


public final class DefaultWallItem extends RoomItemWall {
    public DefaultWallItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity instanceof PlayerEntity))
                return false;

            if (!entity.getRoom().getRights().hasRights(((PlayerEntity) entity).getPlayerId())) {
                return false;
            }
        }

        this.toggleInteract(true);
        this.sendUpdate();

        this.saveData();
        return true;
    }
}
