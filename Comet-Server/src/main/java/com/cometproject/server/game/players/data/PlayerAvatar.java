package com.cometproject.server.game.players.data;

public interface PlayerAvatar {
    public static final byte USERNAME_FIGURE = 0;
    public static final byte USERNAME_FIGURE_MOTTO = 1;

    int getId();

    String getUsername();

    void setUsername(String username);

    String getFigure();

    void setFigure(String figure);

    String getMotto();

    void setMotto(String motto);
}