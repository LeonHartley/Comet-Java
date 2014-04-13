package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.entities.types.data.PlayerBotData;
import com.cometproject.server.game.rooms.types.Room;
import javolution.util.FastMap;

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

            while(data.next()) {
                PetData petData = new PetData(data);
                PetEntity botEntity = new PetEntity(petData, room.getEntities().getFreeId(), new Position3D(data.getInt("x"), data.getInt("y")), 1, 1, room);

                this.petDataInstances.put(petData.getId(), petData);
                this.getRoom().getEntities().addEntity(botEntity);
            }
        } catch(Exception e) {
            room.log.error("Error while deploying bots", e);
        }
    }

    public PetData getPetData(int id) {
        return this.petDataInstances.get(id);
    }

    public PetEntity addPet(PetData pet, int x, int y) {
        /*int virtualId = room.getEntities().getFreeId();

        BotData botData = new PlayerBotData(bot.getId(), bot.getName(), bot.getMotto(), bot.getFigure(), bot.getGender(), bot.getOwnerName(), bot.getOwnerId(), "[]", true, 7);
        BotEntity botEntity = new BotEntity(botData, virtualId, new Position3D(x, y, 0), 1, 1, room);

        this.petDataInstances.put(botData.getId(), botData);
        this.getRoom().getEntities().addEntity(botEntity);
        return botEntity;*/
        return null;
    }

    public void dispose() {
        this.room = null;
    }

    public Room getRoom() {
        return this.room;
    }
}
