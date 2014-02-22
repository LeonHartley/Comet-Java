package com.cometproject.server.game.pets.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PetRace {
    private int id;
    private int petId;

    private int colour1;
    private int colour2;

    public PetRace(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.petId = data.getInt("pet_id");

        this.colour1 = data.getInt("colour_1");
        this.colour2 = data.getInt("colour_2");
    }

    public int getId() {
        return id;
    }

    public int getPetId() {
        return petId;
    }

    public int getColour1() {
        return colour1;
    }

    public int getColour2() {
        return colour2;
    }


    public boolean hasColour1() {
        return colour1 != 0;
    }

    public boolean hasColour2() {
        return colour2 != 0;
    }
}
