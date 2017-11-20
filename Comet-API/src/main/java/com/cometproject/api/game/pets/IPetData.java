package com.cometproject.api.game.pets;

import com.cometproject.api.game.utilities.Position;
import com.google.gson.JsonObject;

public interface IPetData extends IPetStats {

    JsonObject toJsonObject();

    void saveStats();

    void saveHorseData();

    void increaseExperience(int amount);

    void increaseHappiness(int amount);

    void incrementLevel();

    void incrementScratches();

    int getId();

    String getName();

    int getLevel();

    int getHappiness();

    int getExperience();

    int getExperienceGoal();

    int getEnergy();

    int getHunger();

    void decreaseEnergy(int amount);

    void increaseEnergy(int amount);

    int getOwnerId();

    String getColour();

    int getRaceId();

    String getLook();

    int getHairDye();

    int getHair();

    int getTypeId();

    Position getRoomPosition();

    void setRoomPosition(Position position);

    void setHairDye(int hairDye);

    void setHair(int hair);

    boolean isSaddled();

    void setSaddled(boolean saddled);

    boolean isAnyRider();

    void setAnyRider(boolean anyRider);

    int getScratches();

    void setRaceId(int raceId);

    int getBirthday();

    void setBirthday(int birthday);

    String getOwnerName();

    void setOwnerName(String ownerName);

    void setLevel(int level);

    void increaseHunger(int amount);
}
