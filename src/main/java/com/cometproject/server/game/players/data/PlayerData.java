package com.cometproject.server.game.players.data;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;

import java.io.Serializable;
import java.sql.PreparedStatement;

public class PlayerData implements Serializable {
    private int id;
    private int rank;

    private String username;
    private String motto;
    private String figure;
    private String gender;

    private int credits;
    private int points;

    private String regDate;
    private int lastVisit;

    private boolean vip;

    public PlayerData(int id, String username, String motto, String figure, String gender, int rank, int credits, int vipPoints, String reg, int lastVisit, boolean vip) {
        this.id = id;
        this.username = username;
        this.motto = motto;
        this.figure = figure;
        this.rank = rank;
        this.credits = credits;
        this.points = vipPoints;
        this.gender = gender;
        this.regDate = reg;
        this.lastVisit = lastVisit;
        this.vip = vip;
    }

    public boolean save() {
        try {
            PreparedStatement std = Comet.getServer().getStorage().prepare("UPDATE players SET id = ?, username = ?, motto = ?, figure = ?, credits = ?, vip_points = ?, gender = ? WHERE id = ?");
            std.setInt(1, id);
            std.setString(2, username);
            std.setString(3, motto);
            std.setString(4, figure);
            std.setInt(5, credits);
            std.setInt(6, points);
            std.setString(7, gender);
            std.setInt(8, id);

            return std.execute();
        } catch(Exception e) {
            GameEngine.getLogger().error("Error while saving player data", e);
        }

        return false;
    }

    public void decreaseCredits(int amount) {
        this.credits -= amount;
    }

    public void increaseCredits(int amount) {
        this.credits += amount;
    }

    public void decreasePoints(int points) {
        this.points -= points;
    }

    public void increasePoints(int points) {
        this.points += points;
    }

    public int getId() {
        return this.id;
    }

    public int getRank() {
        return this.rank;
    }

    public String getUsername() {
        return this.username;
    }

    public String getMotto() {
        return this.motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getFigure() {
        return this.figure;
    }

    public String getGender() {
        return this.gender;
    }

    public int getCredits() {
        return this.credits;
    }

    public int getPoints() {
        return this.points;
    }

    public int getLastVisit() {
        return this.lastVisit;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public boolean isVip() {
        return this.vip;
    }

    public void setLastVisit(long time) {
        this.lastVisit = (int) time;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
