package com.cometproject.server.game.rooms.objects.items.types;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;


public final class GenericWallItem extends RoomItemWall {
    public GenericWallItem(int id, int itemId, Room room, int owner, String position, String data) {
        super(id, itemId, room, owner, position, data);
    }

    @Override
    public boolean onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
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
