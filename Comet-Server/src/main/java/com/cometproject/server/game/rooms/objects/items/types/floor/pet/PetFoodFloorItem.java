package com.cometproject.server.game.rooms.objects.items.types.floor.pet;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class PetFoodFloorItem extends RoomItemFloor {

    private int state;
    private PetEntity pet;

    public PetFoodFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        this.state = Integer.parseInt(roomItemData.getData());
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (!(entity instanceof PetEntity)) {
            return;
        }

        final PetEntity petEntity = (PetEntity) entity;

        if (petEntity.getData().getHunger() >= 20) {
            this.pet = petEntity;
            this.pet.getPetAI().beginEating();

            final Position lookPos = this.getPosition().squareInFront(this.getRotation());

            this.pet.lookTo(lookPos.getX(), lookPos.getY());
            this.setTicks(RoomItemFactory.getProcessTime(2.0));
        }
    }

    @Override
    public boolean toggleInteract(boolean state) {
        return false;
    }

    @Override
    public void onTickComplete() {
        if (this.pet != null && this.pet.getData().getHunger() >= 20) {
            this.pet.getData().increaseHunger(-20);
            this.pet.getData().increaseHappiness(10);

            this.state++;

            this.getItemData().setData(this.state);
            this.sendUpdate();
            this.saveData();

            if (this.state >= this.getDefinition().getInteractionCycleCount()) {
                // there's no food left!
                // delete the item & free the pet

                if (this.pet.getData().getHunger() >= 20) {
                    this.pet.getPetAI().applyGesture("hng");
                }

                this.pet.getPetAI().eatingComplete();
                this.getRoom().getItems().removeItem(this, null, false, true);
            } else {
                if (this.pet.getData().getHunger() >= 20) {
                    this.setTicks(RoomItemFactory.getProcessTime(2.0));
                } else {
                    // pet is no longer hungry. lets go!
                    this.pet.getPetAI().applyGesture("sml");
                    this.pet.getPetAI().eatingComplete();
                }
            }
        }
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (entity == this.pet) {
            this.pet = null;
        }

        ((PetEntity) entity).getPetAI().eatingComplete(false);
    }
}