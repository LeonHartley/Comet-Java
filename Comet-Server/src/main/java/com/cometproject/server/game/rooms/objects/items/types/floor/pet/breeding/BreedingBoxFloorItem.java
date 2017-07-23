package com.cometproject.server.game.rooms.objects.items.types.floor.pet.breeding;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.pets.PetBreedingMessageComposer;

public abstract class BreedingBoxFloorItem extends DefaultFloorItem {

    private PetEntity mother;
    private PetEntity father;

    public BreedingBoxFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    public void begin() {
        this.setExtraData("1");
        this.sendUpdate();
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity) {
        if (!(entity instanceof PetEntity)) {
            return true;
        }

        final PetEntity petEntity = (PetEntity) entity;

        if (petEntity.getData() != null) {
            if (petEntity.getData().getTypeId() == this.getPetType()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onEntityStepOn(final RoomEntity entity) {
        // begin breeding if there's 2 of the same type of pet.
        if(this.mother == null && entity instanceof PetEntity) {
            if(((PetEntity) entity).getData().getTypeId() == this.getPetType()) {
                this.mother = ((PetEntity) entity);
            }
        } else if(this.father == null && entity instanceof PetEntity) {
            if(((PetEntity) entity).getData().getTypeId() == this.getPetType()) {
                this.father = ((PetEntity) entity);
            }
        }

        if(this.getTile().getEntities().size() == 2) {
            final PlayerEntity playerEntity = this.getRoom().getEntities().getEntityByPlayerId(this.getOwner());
            playerEntity.getPlayer().getSession().send(new PetBreedingMessageComposer(this.getBabyType(), this.mother.getData(), this.father.getData()));
        }
    }

    @Override
    public void onEntityStepOff(final RoomEntity entity) {

    }

    protected abstract int getBabyType();

    protected abstract int getPetType();
}
