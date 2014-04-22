package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.types.Room;
import javolution.util.FastMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM pet_data WHERE room_id = " + this.room.getId());

            while (data.next()) {
                PetData petData = new PetData(data);
                PetEntity botEntity = new PetEntity(petData, room.getEntities().getFreeId(), new Position3D(data.getInt("x"), data.getInt("y")), 1, 1, room);

                this.petDataInstances.put(petData.getId(), petData);
                this.getRoom().getEntities().addEntity(botEntity);
            }
        } catch (Exception e) {
            room.log.error("Error while deploying bots", e);
        }
    }

    public PetData getPetData(int id) {
        return this.petDataInstances.get(id);
    }

    public PetEntity addPet(PetData pet, int x, int y) {
        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE pet_data SET x = ?, y = ?, room_id = ? WHERE id = ?");

            statement.setInt(1, x);
            statement.setInt(2, y);
            statement.setInt(3, this.room.getId());
            statement.setInt(4, pet.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            room.log.error("Error while saving pet to room", e);
            return null;
        }

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
