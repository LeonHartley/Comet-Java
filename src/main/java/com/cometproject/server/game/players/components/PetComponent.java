package com.cometproject.server.game.players.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.players.types.Player;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class PetComponent {
    private Player player;
    private Map<Integer, PetData> pets;

    private Logger log = Logger.getLogger(PetComponent.class.getName());

    public PetComponent(Player player) {
        this.player = player;
        this.pets = new FastMap<>();

        this.loadPets();
    }

    public void loadPets() {
        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM pet_data WHERE owner_id = " + this.player.getId() + " AND room_id = 0");

            while(data.next()) {
                this.pets.put(data.getInt("id"), new PetData(data));
            }
        } catch(Exception e) {
            log.error("Error while loading pets for player: " + player.getId(), e);
        }
    }

    public PetData getPet(int id) {
        if(this.getPets().containsKey(id)) {
            return this.getPets().get(id);
        }

        return null;
    }

    public void addPet(PetData petData) {
        this.pets.put(petData.getId(), petData);
    }

    public void dispose() {
        this.pets.clear();
        this.pets = null;
        this.player = null;
    }

    public Map<Integer, PetData> getPets() {
        return this.pets;
    }
}
