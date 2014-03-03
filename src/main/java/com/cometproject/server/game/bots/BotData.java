package com.cometproject.server.game.bots;

public abstract class BotData implements BotInformation {
    private int id, chatDelay, ownerId;
    private String username, motto, figure, gender, ownerName;
    private boolean isAutomaticChat;
    private String[] chats;

    public BotData(int id, String username, String motto, String figure, String gender, String ownerName, int ownerId) {
        this.id = id;
        this.username = username;
        this.motto = motto;
        this.figure = figure;
        this.gender = gender;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getChatDelay() {
        return this.chatDelay;
    }

    public String[] getChats() {
        return this.chats;
    }

    public boolean isAutomaticChat() {
        return isAutomaticChat;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getOwnerId() {
        return ownerId;
    }
}

