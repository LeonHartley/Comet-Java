package com.cometproject.server.game.rooms.objects.items.types.floor.pet.eggs;

import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.game.pets.races.PetType;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.RandomUtil;

public class PterosaurEggFloorItem extends PetPackageFloorItem {
    public PterosaurEggFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public int getPetTypeId() {
        return PetType.PTEROSAUR;
    }

    @Override
    public int getRaceId() {
        return RandomUtil.getRandomInt(1, 20);
    }
}
