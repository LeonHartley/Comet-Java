package com.cometproject.server.game.bots;

public abstract class BotData implements BotInformation {
    private String username, motto, figure, gender;

    public BotData(String username, String motto, String figure, String gender) {
        this.username = username;
        this.motto = motto;
        this.figure = figure;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}

