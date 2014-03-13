package com.cometproject.server.game.pets.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PetData {
    private int id;
    private String name;
    private int level;
    private int happiness;
    private int experience;
    private int energy;
    private int ownerId;
    private String colour;
    private int raceId;

    public PetData(int id, String name, int level, int happiness, int experience, int energy, int ownerId, String colour, int raceId) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.happiness = happiness;
        this.experience = experience;
        this.energy = energy;
        this.ownerId = ownerId;
        this.colour = colour;
        this.raceId = raceId;
    }

    public PetData(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.name = data.getString("pet_name");
        this.level = data.getInt("level");
        this.happiness = data.getInt("happiness");
        this.experience = data.getInt("experience");
        this.energy = data.getInt("energy");
        this.ownerId = data.getInt("owner_id");
        this.colour = data.getString("colour");
        this.raceId = data.getInt("race_id");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getExperience() {
        return experience;
    }

    public int getEnergy() {
        return energy;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getColour() {
        return colour;
    }

    public int getRaceId() {
        return raceId;
    }
}
