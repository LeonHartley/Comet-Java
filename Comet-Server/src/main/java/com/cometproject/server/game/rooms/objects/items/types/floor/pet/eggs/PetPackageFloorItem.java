package com.cometproject.server.game.rooms.objects.items.types.floor.pet.eggs;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.pets.PetPackageMessageComposer;

public abstract class PetPackageFloorItem extends RoomItemFloor {

    public PetPackageFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }


    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if(isWiredTrigger) {
            return false;
        }

        // send petpackage msg
        if(!(entity instanceof PlayerEntity)) {
            return false;
        }

        final PlayerEntity playerEntity = (PlayerEntity) entity;

        playerEntity.getPlayer().getSession().send(new PetPackageMessageComposer(this.getVirtualId()));
        return true;
    }

    public abstract int getPetTypeId();

    public abstract int getRaceId();
}
