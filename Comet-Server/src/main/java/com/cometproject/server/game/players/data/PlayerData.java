package com.cometproject.server.game.players.data;

import com.cometproject.api.game.players.data.IPlayerData;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.utilities.validator.PlayerFigureValidator;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PlayerData implements IPlayerData {
    public static final String DEFAULT_FIGURE = "hr-100-61.hd-180-2.sh-290-91.ch-210-66.lg-270-82";

    private int id;
    private int rank;

    private Player player;

    private String username;
    private String motto;
    private String figure;
    private String gender;
    private String email;

    private String ipAddress;

    private int credits;
    private int vipPoints;
    private int activityPoints;
    private int seasonalPoints;

    private String regDate;
    private int lastVisit;
    private int regTimestamp;
    private int achievementPoints;

    private int favouriteGroup;

    private String temporaryFigure;

    private boolean vip;
    private int questId;

    private int timeMuted;
    private String nameColour;
    private boolean nameColourChanged;

    private boolean changingName = false;

    private boolean flaggingUser = false;

    private Object tempData = null;

    public PlayerData(int id, String username, String motto, String figure, String gender, String email, int rank, int credits, int vipPoints, int activityPoints,
                      int seasonalPoints, String reg, int lastVisit, boolean vip, int achievementPoints, int regTimestamp, int favouriteGroup, String ipAddress, int questId, int timeMuted, String nameColour, Player player) {
        this.id = id;
        this.username = username;
        this.motto = motto;
        this.figure = figure;
        this.rank = rank;
        this.credits = credits;
        this.vipPoints = vipPoints;
        this.activityPoints = activityPoints;
        this.seasonalPoints = seasonalPoints;
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
        this.timeMuted = timeMuted;
        this.nameColour = nameColour;

        if (this.figure != null) {
            if (!PlayerFigureValidator.isValidFigureCode(this.figure, this.gender.toLowerCase())) {
                this.figure = DEFAULT_FIGURE;
            }
        }

        this.player = player;

        flush();
    }

    public PlayerData(ResultSet data, Player player) throws SQLException {
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
                data.getInt("playerData_seasonalPoints"),
                data.getString("playerData_regDate"),
                data.getInt("playerData_lastOnline"),
                data.getString("playerData_vip").equals("1"),
                data.getInt("playerData_achievementPoints"),
                data.getInt("playerData_regTimestamp"),
                data.getInt("playerData_favouriteGroup"),
                data.getString("playerData_lastIp"),
                data.getInt("playerData_questId"),
                data.getInt("playerData_timeMuted"),
                data.getString("playerData_nameColour"), player);
    }

    @Override
    public Object tempData() {
        return this.tempData;
    }

    @Override
    public void tempData(Object obj) {
        this.tempData = obj;
    }

    public void save() {
        this.saveNow();
    }

    public void saveNow() {
        PlayerDao.updatePlayerData(id, username, motto, figure, credits, vipPoints, gender, favouriteGroup, activityPoints, seasonalPoints, questId, achievementPoints, nameColour);
    }

    public void decreaseCredits(int amount) {
        this.credits -= amount;

        flush();
    }

    public void increaseCredits(int amount) {
        this.credits += amount;

        flush();
    }

    public void decreaseVipPoints(int points) {
        this.vipPoints -= points;

        flush();
    }

    public void increaseVipPoints(int points) {
        this.vipPoints += points;

        flush();
    }

    public void increaseActivityPoints(int points) {
        this.activityPoints += points;

        flush();
    }

    public void decreaseActivityPoints(int points) {
        this.activityPoints -= points;

        flush();
    }

    public void increaseSeasonalPoints(int points) {
        this.seasonalPoints += points;

        flush();
    }

    public void decreaseSeasonalPoints(int points) {
        this.seasonalPoints -= points;

        flush();
    }

    public void increaseAchievementPoints(int points) {
        this.achievementPoints += points;

        flush();
    }

    public int getId() {
        return this.id;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;

        flush();
    }

    public int getAchievementPoints() {
        return this.achievementPoints;
    }

    public void setAchievementPoints(int achievementPoints) {
        this.achievementPoints = achievementPoints;
    }

    public String getMotto() {
        return this.motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;

        flush();
    }

    public String getFigure() {
        return this.figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;

        flush();
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;

        flush();
    }

    public int getCredits() {
        return this.credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;

        flush();
    }

    public int getVipPoints() {
        return this.vipPoints;
    }

    public void setVipPoints(int vipPoints) {
        this.vipPoints = vipPoints;
    }

    public int getLastVisit() {
        return this.lastVisit;
    }

    public void setLastVisit(long time) {
        this.lastVisit = (int) time;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public boolean isVip() {
        return this.vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;

        flush();
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

    public int getTimeMuted() {
        return this.timeMuted;
    }

    public void setTimeMuted(int Time) {
        this.timeMuted = Time;

        flush();
    }

    public boolean getChangingName() {
        return this.changingName;
    }

    public void setChangingName(boolean changingName) {
        this.changingName = changingName;

        flush();
    }

    public boolean getFlaggingUser() {
        return this.flaggingUser;
    }

    public void setFlaggingUser(boolean flaggingUser) {
        this.flaggingUser = flaggingUser;

        flush();
    }

    public String getNameColour() {
        return this.nameColour;
    }

    @Override
    public void setNameColour(String nameColour) {
        this.nameColour = nameColour;
        this.nameColourChanged = true;

        flush();
    }

    public int getSeasonalPoints() {
        return seasonalPoints;
    }

    public void setSeasonalPoints(int seasonalPoints) {
        this.seasonalPoints = seasonalPoints;

        flush();
    }

    public Player getPlayer() {
        return player;
    }

    public void flush() {
        if (getPlayer() != null) {
            getPlayer().flush();
        }
    }

    public boolean nameColourChanged() {
        return this.nameColourChanged;
    }

    public void setNameColourChanged(boolean changed) {
        this.nameColourChanged = changed;
    }
}
