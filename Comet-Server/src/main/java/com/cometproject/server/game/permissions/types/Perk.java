package com.cometproject.server.game.permissions.types;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Perk {
    private final int id;
    private final String title;
    private final String data;
    private final int rank;
    private final boolean overrideRank;
    private final boolean overrideDefault;

    public Perk(int id, String title, String data, int rank, boolean overrideRank, boolean overrideDefault) {
        this.id = id;
        this.title = title;
        this.data = data;
        this.rank = rank;
        this.overrideRank = overrideRank;
        this.overrideDefault = overrideDefault;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getData() {
        return this.data;
    }

    public int getRank() {
        return this.rank;
    }

    public boolean doesOverride() {
        return this.overrideRank;
    }

    public boolean getDefault() {
        return this.overrideDefault;
    }
}

