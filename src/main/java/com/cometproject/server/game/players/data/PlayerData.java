package com.cometproject.server.game.players.data;

import com.cometproject.server.storage.queries.player.PlayerDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerData {
    private int id;
    private int rank;

    private String username;
    private String motto;
    private String figure;
    private String gender;
    private String email;

    private String ipAddress;

    private int credits;
    private int points;

    private String regDate;
    private int lastVisit;
    private int regTimestamp;
    private int achievementPoints;

    private int favouriteGroup;

    private boolean vip;

    public PlayerData(int id, String username, String motto, String figure, String gender, String email, int rank, int credits, int vipPoints,
                      String reg, int lastVisit, boolean vip, int achievementPoints, int regTimestamp, int favouriteGroup, String ipAddress) {
        this.id = id;
        this.username = username;
        this.motto = motto;
        this.figure = figure;
        this.rank = rank;
        this.credits = credits;
        this.points = vipPoints;
        this.gender = gender;
        this.vip = vip;
        this.achievementPoints = achievementPoints;
        this.email = email;

        this.regDate = reg;
        this.lastVisit = lastVisit;
        this.regTimestamp = regTimestamp;

        this.favouriteGroup = favouriteGroup;
        this.ipAddress = ipAddress;
    }

    public PlayerData(ResultSet data) throws SQLException {
        this.id = data.getInt("playerId");
        this.username = data.getString("playerData_username");
        this.motto = data.getString("playerData_motto");
        this.figure = data.getString("playerData_figure");
        this.gender = data.getString("playerData_gender");
        this.email = data.getString("playerData_email");
        this.rank = data.getInt("playerData_rank");
        this.credits = data.getInt("playerData_credits");
        this.points = data.getInt("playerData_vipPoints");
        this.regDate = data.getString("playerData_regDate");
        this.lastVisit = data.getInt("playerData_lastOnline");
        this.vip = data.getString("playerData_vip").equals("1");
        this.achievementPoints = data.getInt("playerData_achievementPoints");
        this.regTimestamp = data.getInt("playerData_regTimestamp");
        this.favouriteGroup = data.getInt("playerData_favouriteGroup");
    }

    public void save() {
        PlayerDao.updatePlayerData(id, username, motto, figure, credits, points, gender, favouriteGroup);
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

    public void setPoints(int points) {
        this.points = points;
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

    public int getRegTimestamp() {
        return regTimestamp;
    }

    public void setRegTimestamp(int regTimestamp) {
        this.regTimestamp = regTimestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFavouriteGroup() {
        return favouriteGroup;
    }

    public void setFavouriteGroup(int favouriteGroup) {
        this.favouriteGroup = favouriteGroup;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
