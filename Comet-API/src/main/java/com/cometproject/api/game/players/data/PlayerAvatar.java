package com.cometproject.api.game.players.data;

public interface PlayerAvatar {
    byte USERNAME_FIGURE = 0;
    byte USERNAME_FIGURE_MOTTO = 1;

    int getId();

    String getUsername();

    void setUsername(String username);

    String getFigure();

    void setFigure(String figure);

    String getMotto();

    void setMotto(String motto);

    String getGender();

    void setGender(String gender);

    default void tempData(final Object tempData) {
    }

    default Object tempData() {
        return null;
    }

}