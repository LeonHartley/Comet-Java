package com.cometproject.storage.mysql.models.landing;

import com.cometproject.api.game.landing.types.IHallOfFame;

/**
 * Created by SpreedBlood on 2017-12-27.
 */
public class HallOfFame implements IHallOfFame {

    private int id;
    private int currency;
    private String username;
    private String figure;

    public HallOfFame(int id, int currency, String username, String figure) {
        this.id = id;
        this.currency = currency;
        this.username = username;
        this.figure = figure;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getCurrency() {
        return currency;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getFigure() {
        return figure;
    }

}
