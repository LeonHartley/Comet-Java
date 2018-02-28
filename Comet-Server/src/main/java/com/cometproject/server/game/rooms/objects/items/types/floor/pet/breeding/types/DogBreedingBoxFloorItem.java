package com.cometproject.server.game.rooms.objects.items.types.floor.pet.breeding.types;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.pets.races.PetType;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.breeding.BreedingBoxFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class DogBreedingBoxFloorItem extends BreedingBoxFloorItem {
    public DogBreedingBoxFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    protected int getBabyType() {
        return PetType.PUPPY;
    }

    @Override
    protected int getPetType() {
        return PetType.DOG;
    }
}
