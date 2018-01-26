package com.cometproject.server.game.rooms.objects.items.types.floor.pet.eggs;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.pets.races.PetType;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.RandomUtil;

public class VelociraptorEggFloorItem extends PetPackageFloorItem {

    public VelociraptorEggFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getPetTypeId() {
        return PetType.VELOCIRAPTOR;
    }

    @Override
    public int getRaceId() {
        return RandomUtil.getRandomInt(1, 20);
    }
}
