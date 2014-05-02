package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.storage.queries.pets.RoomPetDao;
import javolution.util.FastMap;

import java.util.Map;

public class PetComponent {
    private Room room;

    private Map<Integer, PetData> petDataInstances;

    public PetComponent(Room room) {
        this.room = room;
        this.petDataInstances = new FastMap<>();

        this.load();
    }

    public void load() {
        try {

        } catch (Exception e) {
            room.log.error("Error while deploying bots", e);
        }
    }

    public PetData getPetData(int id) {
        return this.petDataInstances.get(id);
    }

    public PetEntity addPet(PetData pet, int x, int y) {
        RoomPetDao.updatePet(this.room.getId(), x, y, pet.getId());

        int virtualId = room.getEntities().getFreeId();

        PetEntity petEntity = new PetEntity(pet, virtualId, new Position3D(x, y, 0), 1, 1, room);

        this.petDataInstances.put(pet.getId(), pet);
        this.getRoom().getEntities().addEntity(petEntity);

        return petEntity;
    }

    public void dispose() {
        this.petDataInstances.clear();
        this.petDataInstances = null;

        this.room = null;
    }

    public Room getRoom() {
        return this.room;
    }
}
