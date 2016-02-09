package com.cometproject.server.game.players.data;

import com.cometproject.api.game.players.data.IPlayerData;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.utilities.validator.PlayerFigureValidator;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queue.types.PlayerDataStorageQueue;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PlayerData implements PlayerAvatar, IPlayerData {
    public static final String DEFAULT_FIGURE = "hr-100-61.hd-180-2.sh-290-91.ch-210-66.lg-270-82";

    private int id;
    private int rank;

    private String username;
    private String motto;
    private String figure;
    private String gender;
    private String email;

    private String ipAddress;

    private int credits;
    private int vipPoints;
    private int activityPoints;

    private String regDate;
    private int lastVisit;
    private int regTimestamp;
    private int achievementPoints;

    private int favouriteGroup;

    private String temporaryFigure;

    private boolean vip;
    private int questId;

    public PlayerData(int id, String username, String motto, String figure, String gender, String email, int rank, int credits, int vipPoints, int activityPoints,
                      String reg, int lastVisit, boolean vip, int achievementPoints, int regTimestamp, int favouriteGroup, String ipAddress, int questId) {
        this.id = id;
        this.username = username;
        this.motto = motto;
        this.figure = figure;
        this.rank = rank;
        this.credits = credits;
        this.vipPoints = vipPoints;
        this.activityPoints = activityPoints;
        this.gender = gender;
        this.vip = vip;
        this.achievementPoints = achievementPoints;
        this.email = email;
        this.regDate = reg;
        this.lastVisit = lastVisit;
        this.regTimestamp = regTimestamp;
        this.favouriteGroup = favouriteGroup;
        this.ipAddress = ipAddress;
        this.questId = questId;

        if(this.figure != null) {
            if (!PlayerFigureValidator.isValidFigureCode(this.figure, this.gender.toLowerCase())) {
                this.figure = DEFAULT_FIGURE;
            }
        }
    }

    public PlayerData(ResultSet data) throws SQLException {
        this(data.getInt("playerId"),
                data.getString("playerData_username"),
                data.getString("playerData_motto"),
                data.getString("playerData_figure"),
                data.getString("playerData_gender"),
                data.getString("playerData_email"),
                data.getInt("playerData_rank"),
                data.getInt("playerData_credits"),
                data.getInt("playerData_vipPoints"),
                data.getInt("playerData_activityPoints"),
                data.getString("playerData_regDate"),
                data.getInt("playerData_lastOnline"),
                data.getString("playerData_vip").equals("1"),
                data.getInt("playerData_achievementPoints"),
                data.getInt("playerData_regTimestamp"),
                data.getInt("playerData_favouriteGroup"),
                data.getString("playerData_lastIp"),
                data.getInt("playerData_questId"));
    }

    public void save() {
        if(CometSettings.storagePlayerQueueEnabled) {
            PlayerDataStorageQueue.getInstance().queueSave(this);
        } else {
            this.saveNow();
        }
    }

    public void saveNow() {
        PlayerDao.updatePlayerData(id, username, motto, figure, credits, vipPoints, gender, favouriteGroup, activityPoints, questId, achievementPoints);
    }

    public void decreaseCredits(int amount) {
        this.credits -= amount;
    }

    public void increaseCredits(int amount) {
        this.credits += amount;
    }

    public void decreasePoints(int points) {
        this.vipPoints -= points;
    }

    public void increasePoints(int points) {
        this.vipPoints += points;
    }

    public void increaseActivityPoints(int points) {
        this.activityPoints += points;
    }

    public void decreaseActivityPoints(int points) {
        this.activityPoints -= points;
    }

    public void increaseAchievementPoints(int points) {
        this.achievementPoints += points;
    }

    public void setPoints(int points) {
        this.vipPoints = points;
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

    public void setUsername(String username) {
        this.username = username;
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

    public int getVipPoints() {
        return this.vipPoints;
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

    public int getActivityPoints() {
        return activityPoints;
    }

    public void setActivityPoints(int activityPoints) {
        this.activityPoints = activityPoints;
    }

    public void setVipPoints(int vipPoints) {
        this.vipPoints = vipPoints;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTemporaryFigure() {
        return temporaryFigure;
    }

    public void setTemporaryFigure(String temporaryFigure) {
        this.temporaryFigure = temporaryFigure;
    }

    public int getQuestId() {
        return questId;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }

    public void setAchievementPoints(int achievementPoints) {
        this.achievementPoints = achievementPoints;
    }
}
