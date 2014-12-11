package com.cometproject.server.game.pets.data;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.objects.misc.Position;

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
    private int typeId;

    private int hairDye = 0;
    private int hair = -1;

    private Position roomPosition;

    public PetData(int id, String name, int level, int happiness, int experience, int energy, int ownerId, String colour, int raceId, int typeId) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.happiness = happiness;
        this.experience = experience;
        this.energy = energy;
        this.ownerId = ownerId;
        this.colour = colour;
        this.raceId = raceId;
        this.typeId = typeId;
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
        this.typeId = data.getInt("type");
        this.roomPosition = new Position(data.getInt("x"), data.getInt("y"));
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

    public String getLook() {
        return this.getTypeId() + " " + this.getRaceId() + " " + this.getColour();
    }

    public int getHairDye() {
        return hairDye;
    }

    public int getHair() {
        return this.hair;
    }

    public int getTypeId() {
        return typeId;
    }

    public String[] getSpeech() {
        return CometManager.getPets().getSpeech(this.typeId);
    }

    public Position getRoomPosition() {
        return this.roomPosition;
    }

    public void setRoomPosition(Position position) {
        this.roomPosition = position;
    }
}
