package com.cometproject.server.game.pets.data;

import com.cometproject.server.game.pets.PetManager;
import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.storage.queries.pets.PetDao;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PetData {
    private int id;
    private String name;

    private int scratches;
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

    private boolean anyRider = false;
    private boolean saddled = false;

    private int birthday;

    private Position roomPosition;

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
        this.saddled = data.getBoolean("saddled");
        this.hair = data.getInt("hair_style");
        this.hairDye = data.getInt("hair_colour");
        this.anyRider = data.getBoolean("any_rider");
        this.birthday = data.getInt("birthday");

        this.roomPosition = new Position(data.getInt("x"), data.getInt("y"));
    }

    public PetData(int id, String name, int scratches, int level, int happiness, int experience, int energy, int ownerId, String colour, int raceId, int typeId) {
        this.id = id;
        this.name = name;
        this.scratches = scratches;
        this.level = level;
        this.happiness = happiness;
        this.experience = experience;
        this.energy = energy;
        this.ownerId = ownerId;
        this.colour = colour;
        this.raceId = raceId;
        this.typeId = typeId;
    }

    public void saveStats() {
        PetDao.saveStats(this.scratches, this.level, this.happiness, this.experience, this.energy, this.id);
    }

    public void saveHorseData() {
        PetDao.saveHorseData(this.getId(), this.isSaddled(), this.hair, this.hairDye, this.anyRider, this.raceId);
    }

    public void increaseExperience(int amount) {
        this.experience += amount;
    }

    public void increaseHappiness(int amount) {
        this.happiness += amount;
    }

    public void incrementLevel() {
        this.level += 1;
    }

    public void incrementScratches() {
        this.scratches += 1;
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
        return this.hairDye;
    }

    public int getHair() {
        return this.hair;
    }

    public int getTypeId() {
        return typeId;
    }

    public PetSpeech getSpeech() {
        return PetManager.getInstance().getSpeech(this.typeId);
    }

    public Position getRoomPosition() {
        return this.roomPosition;
    }

    public void setRoomPosition(Position position) {
        this.roomPosition = position;
    }

    public void setHairDye(int hairDye) {
        this.hairDye = hairDye;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public boolean isSaddled() {
        return saddled;
    }

    public void setSaddled(boolean saddled) {
        this.saddled = saddled;
    }

    public boolean isAnyRider() {
        return anyRider;
    }

    public void setAnyRider(boolean anyRider) {
        this.anyRider = anyRider;
    }

    public int getScratches() {
        return scratches;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }
}
