package com.cometproject.api.game.players.data;

public interface IPlayerStatistics {
    public void save();

    public void incrementAchievementPoints(int amount);

    public void incrementCautions(int amount);

    public void incrementRespectPoints(int amount);

    public void decrementDailyRespects(int amount);

    public void incrementBans(int amount);

    public void incrementAbusiveHelpTickets(int amount);

    public int getPlayerId();

    public int getDailyRespects();

    public int getRespectPoints();

    public int getAchievementPoints();

    public int getFriendCount();

    public int getHelpTickets();

    public void setHelpTickets(int helpTickets);

    public int getAbusiveHelpTickets();

    public void setAbusiveHelpTickets(int abusiveHelpTickets);

    public int getCautions();

    public void setCautions(int cautions);

    public int getBans();

    public void setBans(int bans);
}
