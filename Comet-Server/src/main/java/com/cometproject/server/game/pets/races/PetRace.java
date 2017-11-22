package com.cometproject.server.game.pets.races;

import com.cometproject.api.game.pets.IPetRace;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PetRace implements IPetRace {
    private int raceId;
    private int colour1;
    private int colour2;

    private boolean hasColour1;
    private boolean hasColour2;

    public PetRace(ResultSet data) throws SQLException {
        this.raceId = data.getInt("race_id");

        this.colour1 = data.getInt("colour1");
        this.colour2 = data.getInt("colour2");

        this.hasColour1 = data.getString("has1colour").equals("1");
        this.hasColour2 = data.getString("has2colour").equals("1");
    }

    @Override
    public int getRaceId() {
        return raceId;
    }

    @Override
    public int getColour1() {
        return colour1;
    }

    @Override
    public int getColour2() {
        return colour2;
    }

    @Override
    public boolean hasColour1() {
        return hasColour1;
    }

    @Override
    public boolean hasColour2() {
        return hasColour2;
    }
}