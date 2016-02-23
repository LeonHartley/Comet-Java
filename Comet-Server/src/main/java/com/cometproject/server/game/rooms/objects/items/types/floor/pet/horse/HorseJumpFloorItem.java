package com.cometproject.server.game.rooms.objects.items.types.floor.pet.horse;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class HorseJumpFloorItem extends DefaultFloorItem {
    public HorseJumpFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if(entity instanceof PetEntity && ((PetEntity) entity).getData().getTypeId() == 15) {
            entity.addStatus(RoomEntityStatus.JUMP, "0");
        }
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if(entity instanceof PetEntity && ((PetEntity) entity).getData().getTypeId() == 15) {
            entity.removeStatus(RoomEntityStatus.JUMP);
        }
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity) {
        if(entity.getMountedEntity() == null) {
            return true;
        }

        return false;
    }
}
