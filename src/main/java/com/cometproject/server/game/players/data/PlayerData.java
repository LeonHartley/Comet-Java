package com.cometproject.server.game.players.data;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.sql.PreparedStatement;

public class PlayerData {
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
    private int achievementPoints;

    private boolean vip;

    public PlayerData(int id, String username, String motto, String figure, String gender, int rank, int credits, int vipPoints, String reg, int lastVisit, boolean vip, int achievementPoints) {
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
        this.achievementPoints = achievementPoints;
    }

    public void save() {
        PlayerDao.updatePlayerData(id, username, motto, figure, credits, points, gender);
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

    public int getAchievementPoints() {
        return this.achievementPoints;
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

    public void setCredits(int credits) {
        this.credits = credits;
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

    public void setVip(boolean vip) {
        this.vip = vip;
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
