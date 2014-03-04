package com.cometproject.server.game.bots;

import com.cometproject.server.boot.Comet;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;

public abstract class BotData implements BotInformation {
    private int id, chatDelay, ownerId;
    private String username, motto, figure, gender, ownerName;
    private boolean isAutomaticChat;
    private String[] messages;
    private Logger log = Logger.getLogger(BotData.class.getName());

    public BotData(int id, String username, String motto, String figure, String gender, String ownerName, int ownerId, String messages, boolean automaticChat, int chatDelay) {
        this.id = id;
        this.username = username;
        this.motto = motto;
        this.figure = figure;
        this.gender = gender;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.messages = (messages == null || messages.isEmpty()) ? new String[0] : new Gson().fromJson(messages, String[].class);
        this.chatDelay = chatDelay;
        this.isAutomaticChat = automaticChat;
    }

    public void save() {
        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE bots SET figure = ?, gender = ?, motto = ?, name = ?, messages = ?, automatic_chat = ?, chat_delay = ? WHERE id = ?");

            statement.setString(1, this.getFigure());
            statement.setString(2, this.getGender());
            statement.setString(3, this.getMotto());
            statement.setString(4, this.getUsername());
            statement.setString(5, new Gson().toJson(messages));
            statement.setString(6, isAutomaticChat ? "1" : "0");
            statement.setInt(7, chatDelay);

            statement.setInt(8, this.getId());

            statement.executeUpdate();

        } catch(Exception e) {
            log.error("Error while saving bot data", e);
        }
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

    public void setChatDelay(int delay) {
        this.chatDelay = delay;
    }

    public String[] getMessages() {
        return this.messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public boolean isAutomaticChat() {
        return isAutomaticChat;
    }

    public void setAutomaticChat(boolean isAutomaticChat) {
        this.isAutomaticChat = isAutomaticChat;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getOwnerId() {
        return ownerId;
    }
}

