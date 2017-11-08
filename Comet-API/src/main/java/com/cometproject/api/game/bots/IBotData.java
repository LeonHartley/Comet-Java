package com.cometproject.api.game.bots;

import com.google.gson.JsonObject;

public interface IBotData {
    JsonObject toJsonObject();

    String getRandomMessage();

    void save();

    int getId();

    String getUsername();

    void setUsername(String username);

    String getMotto();

    void setMotto(String motto);

    String getFigure();

    void setFigure(String figure);

    String getGender();

    void setGender(String gender);

    int getChatDelay();

    void setChatDelay(int delay);

    String[] getMessages();

    void setMessages(String[] messages);

    boolean isAutomaticChat();

    void setAutomaticChat(boolean isAutomaticChat);

    String getOwnerName();

    int getOwnerId();

    void dispose();

    BotType getBotType();

    BotMode getMode();

    void setMode(BotMode mode);

    String getData();

    void setData(String data);
}
