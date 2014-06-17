package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.storage.queries.pets.RoomPetDao;
import javolution.util.FastMap;

import java.util.List;
import java.util.Map;

public class PetComponent {
    private Room room;

    public PetComponent(Room room) {
        this.room = room;

        this.load();
    }

    public void load() {
        for (PetData data : RoomPetDao.getPetsByRoomId(this.room.getId())) {
            PetEntity petEntity = new PetEntity(data, room.getEntities().getFreeId(), data.getRoomPosition(), 3, 3, room);
            this.getRoom().getEntities().addEntity(petEntity);
        }
    }

    public PetEntity addPet(PetData pet, int x, int y) {
        RoomPetDao.updatePet(this.room.getId(), x, y, pet.getId());

        int virtualId = room.getEntities().getFreeId();
        PetEntity petEntity = new PetEntity(pet, virtualId, new Position3D(x, y, 0), 3, 3, room);
        this.getRoom().getEntities().addEntity(petEntity);

        return petEntity;
    }

    public void dispose() {
        this.room = null;
    }

    public Room getRoom() {
        return this.room;
    }
}
