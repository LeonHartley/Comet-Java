package com.cometproject.server.game.rooms.objects.items.types.floor.pet;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class PetNestFloorItem extends DefaultFloorItem {
    private PetEntity petEntity;

    public PetNestFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if(!(entity instanceof PetEntity)) {
            return;
        }

        final PetEntity petEntity = (PetEntity) entity;

        this.petEntity = petEntity;

        petEntity.getPetAI().beginNesting();
        this.setTicks(RoomItemFactory.getProcessTime(30.0));
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if(!(entity instanceof PetEntity)) {
            return;
        }

        this.petEntity = null;

        this.cancelTicks();
    }

    @Override
    public void onTickComplete() {
        if(this.petEntity != null) {
            this.petEntity.getPetAI().nestingComplete();
        }
    }
}
