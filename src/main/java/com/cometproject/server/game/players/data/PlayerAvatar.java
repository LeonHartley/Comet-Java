package com.cometproject.server.game.players.data;

public interface PlayerAvatar {
    public int getId();

    public String getUsername();

    public void setUsername(String username);

    public String getFigure();

    public void setFigure(String figure);

    public String getMotto();

    public void setMotto(String motto);
}