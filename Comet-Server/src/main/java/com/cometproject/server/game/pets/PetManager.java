package com.cometproject.server.game.pets;

import com.cometproject.api.game.pets.IPetData;
import com.cometproject.api.game.pets.IPetRace;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.game.pets.data.PetSpeech;
import com.cometproject.server.game.pets.races.PetBreedLevel;
import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.storage.queries.pets.PetDao;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class PetManager implements Initialisable {
    private static PetManager petManagerInstance;
    private final Map<Integer, IPetData> pendingPetDataSaves = Maps.newConcurrentMap();
    private Logger log = Logger.getLogger(PetManager.class.getName());
    private List<PetRace> petRaces;
    private Map<Integer, PetSpeech> petMessages;
    private Map<String, String> transformablePets;
    private Map<Integer, Map<PetBreedLevel, Set<Integer>>> petBreedPallets;

    public PetManager() {

    }

    public static PetManager getInstance() {
        if (petManagerInstance == null)
            petManagerInstance = new PetManager();

        return petManagerInstance;
    }

    @Override
    public void initialize() {
        this.loadPetRaces();
        this.loadPetBreedPallets();
        this.loadPetSpeech();
        this.loadTransformablePets();

        // Set up the queue for saving pet data
        // CometThreadManager.getInstance().executePeriodic(this::savePetStats, 1000, 1000, TimeUnit.MILLISECONDS);

        log.info("PetManager initialized");
    }

    public void loadPetRaces() {
        if (this.petRaces != null) {
            this.petRaces.clear();
        }

        try {
            this.petRaces = PetDao.getRaces();

            log.info("Loaded " + this.petRaces.size() + " pet races");
        } catch (Exception e) {
            log.error("Error while loading pet races", e);
        }
    }

    public void loadPetBreedPallets() {
        if (this.petBreedPallets != null) {
            this.petBreedPallets.clear();
        }

        try {
            this.petBreedPallets = PetDao.getPetBreedPallets();

            log.info("Loaded " + this.petBreedPallets.size() + " pet breed pallet sets");
        } catch (Exception e) {
            log.error("Error while loading pet breed pallets", e);
        }
    }

    public void loadPetSpeech() {
        if (this.petMessages != null) {
            this.petMessages.clear();
        }

        try {
            AtomicInteger petSpeechCount = new AtomicInteger(0);
            this.petMessages = PetDao.getMessages(petSpeechCount);

            log.info("Loaded " + this.petMessages.size() + " pet message sets and " + petSpeechCount.get() + " total messages");
        } catch (Exception e) {
            log.error("Error while loading pet messages");
        }
    }

    public void loadTransformablePets() {
        if (this.transformablePets != null) {
            this.transformablePets.clear();
        }

        try {
            this.transformablePets = PetDao.getTransformablePets();

            log.info("Loaded " + this.transformablePets.size() + " transformable pets");
        } catch (Exception e) {
            log.error("Error while loading transformable pets");
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

    public List<IPetRace> getRacesByRaceId(int raceId) {
        List<IPetRace> races = new ArrayList<>();

        for (PetRace race : this.getPetRaces()) {
            if (raceId == race.getRaceId())
                races.add(race);
        }

        return races;
    }

    public List<PetRace> getPetRaces() {
        return this.petRaces;
    }

    public PetSpeech getSpeech(int petType) {
        return this.petMessages.get(petType);
    }

    public Map<String, String> getTransformablePets() {
        return transformablePets;
    }

    public String getTransformationData(String type) {
        return this.transformablePets.get(type);
    }

    public Map<Integer, Map<PetBreedLevel, Set<Integer>>> getPetBreedPallets() {
        return petBreedPallets;
    }

    //    public String[] getSpeech(int petType) {
//        return this.petSpeech.get(petType);
//    }
}
