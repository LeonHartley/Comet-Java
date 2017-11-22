package com.cometproject.api.game.players.data.types;

public interface IWardrobeItem {
    int getSlot();

    void setSlot(int slot);

    String getGender();

    void setGender(String gender);

    String getFigure();

    void setFigure(String figure);
}
