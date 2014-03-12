package com.cometproject.server.game.pets;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.pets.races.PetRace;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PetManager {
    private Logger log = Logger.getLogger(PetManager.class.getName());

    private List<PetRace> petRaces;

    public PetManager() {
        this.loadPetRaces();
    }

    public void loadPetRaces() {
        if(this.petRaces != null) {
            this.petRaces.clear();
        } else {
            this.petRaces = new FastList<>();
        }

        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM pet_races");

            while(data.next()) {
                this.petRaces.add(new PetRace(data));
            }

            log.info("Loaded " + this.petRaces.size() + " pet races");
        } catch (SQLException e) {
            log.error("Error while loading pet races");
        }
    }

    public int validatePetName(String petName) {
        String pattern= "^[a-zA-Z0-9]*$";

        if(petName.length() <= 0) {
            return 1;
        }

        if(petName.length() > 16) {
            return 2;
        }

        if(!petName.matches(pattern)){
            return 3;
        }

        // WORD FILTER

        return 0;
    }

    public List<PetRace> getRacesByRaceId(int raceId) {
        List<PetRace> races = new FastList<>();

        for(PetRace race : this.getPetRaces()) {
            if(raceId == race.getRaceId())
                races.add(race);
        }

        return races;
    }

    public List<PetRace> getPetRaces() {
        return this.petRaces;
    }
}
