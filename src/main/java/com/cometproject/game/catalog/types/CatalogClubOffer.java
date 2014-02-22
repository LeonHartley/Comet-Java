package com.cometproject.game.catalog.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CatalogClubOffer {
    private int id;
    private String name;
    private int type;
    private int cost;
    private int lengthDays;

    public CatalogClubOffer(ResultSet offer) throws SQLException {
        this.id = offer.getInt("id");
        this.name = offer.getString("name");
        this.type = offer.getInt("level");
        this.cost = offer.getInt("cost");
        this.lengthDays = offer.getInt("length_days");

        // Types:
        //	0 = HC
        // 	1 = VIP
        //	2 = VIP UPGRADE
    }

    public boolean isUpgrade() {
        return this.type == 2;
    }

    public int lengthSeconds() {
        return (86400 * this.lengthDays());
    }

    public int lengthMonths() {
        int correctedLength = this.lengthDays;

        if(this.isUpgrade()) {
            correctedLength += 31;
        }

        return (int) Math.ceil((double)(correctedLength / 31));
    }

    public int lengthDays() {
        return this.lengthDays;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getCost() {
        return this.cost;
    }

    public int getType() {
        return this.type;
    }
}
