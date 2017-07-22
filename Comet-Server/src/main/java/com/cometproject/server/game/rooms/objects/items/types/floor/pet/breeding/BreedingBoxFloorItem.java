package com.cometproject.server.game.rooms.objects.items.types.floor.pet.breeding;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public abstract class BreedingBoxFloorItem extends DefaultFloorItem {
    public BreedingBoxFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity) {
        if (!(entity instanceof PetEntity)) {
            return true;
        }

        final PetEntity petEntity = (PetEntity) entity;

        if (petEntity.getData() != null) {
            if (petEntity.getData().getTypeId() == this.getPetType()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onEntityStepOn(final RoomEntity entity) {
        // begin breeding if there's 2 of the same type of pet.
    }

    @Override
    public void onEntityStepOff(final RoomEntity entity) {

    }

    protected abstract int getPetType();
}
