package com.cometproject.server.game.pets;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.storage.queries.pets.PetDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PetManager {
    private Logger log = Logger.getLogger(PetManager.class.getName());

    private List<PetRace> petRaces;
    private Map<Integer, String[]> petSpeech;

    public PetManager() {
        this.loadPetRaces();
        this.loadPetSpeech();
    }

    public void loadPetRaces() {
        if (this.petRaces != null) {
            this.petRaces.clear();
        } else {
            this.petRaces = new ArrayList<>();
        }

        try {
            this.petRaces = PetDao.getRaces();

            log.info("Loaded " + this.petRaces.size() + " pet races");
        } catch (Exception e) {
            log.error("Error while loading pet races", e);
        }
    }

    public void loadPetSpeech() {
        if (this.petSpeech != null) {
            this.petSpeech.clear();
        } else {
            this.petSpeech = new FastMap<>();
        }

        try {
            for (Map.Entry<String, String> localeEntry : Locale.getAll().entrySet()) {
                if (localeEntry.getKey().startsWith("game.pet.") && localeEntry.getKey().endsWith(".speech")) {
                    int petType = Integer.parseInt(localeEntry.getKey().split("\\.")[2]);
                    String[] speeches = localeEntry.getValue().split(",");

                    this.petSpeech.put(petType, speeches);
                }
            }

            log.info("Loaded " + this.petSpeech.size() + " pet speech sets");
        } catch (Exception e) {
            log.error("Error while loading pet speech", e);
        }
    }

    public int validatePetName(String petName) {
        String pattern = "^[a-zA-Z0-9]*$";

        if (petName.length() <= 0) {
            return 1;
        }

        if (petName.length() > 16) {
            return 2;
        }

        if (!petName.matches(pattern)) {
            return 3;
        }

        // WORD FILTER

        return 0;
    }

    public List<PetRace> getRacesByRaceId(int raceId) {
        List<PetRace> races = new ArrayList<>();

        for (PetRace race : this.getPetRaces()) {
            if (raceId == race.getRaceId())
                races.add(race);
        }

        return races;
    }

    public List<PetRace> getPetRaces() {
        return this.petRaces;
    }

    public String[] getSpeech(int petType) {
        return this.petSpeech.get(petType);
    }
}
