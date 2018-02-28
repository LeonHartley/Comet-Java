package com.cometproject.server.game.bots;

import com.cometproject.api.game.bots.BotMode;
import com.cometproject.api.game.bots.BotType;
import com.cometproject.api.game.bots.IBotData;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.storage.queries.bots.RoomBotDao;
import com.cometproject.server.utilities.RandomUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Arrays;


public abstract class BotData implements IBotData {
    /**
     * The ID of the bot
     */
    private int id;

    /**
     * How long before the bot will chat in the room?
     */
    private int chatDelay;

    /**
     * The ID of the player who owns the bot
     */
    private int ownerId;

    /**
     * The name of the bot
     */
    private String username;

    /**
     * The motto of the bot
     */
    private String motto;

    /**
     * The figure of the bot
     */
    private String figure;

    /**
     * The gender of the bot
     */
    private String gender;

    /**
     * The name of the 1bot's owner
     */
    private String ownerName;

    /**
     * Can the bot talk without being triggered (currently the only way it can...)
     */
    private boolean isAutomaticChat;

    /**
     * The messages the bot can say
     */
    private String[] messages;

    private BotType botType;

    private BotMode mode;

    private String data;

    /**
     * Initialize the bot
     *
     * @param id            The ID of the bot
     * @param username      The name of the bot
     * @param motto         The motto of the bot
     * @param figure        The figure of the bot
     * @param gender        The gender of the bot
     * @param ownerName     The name of the 1 owner
     * @param ownerId       The ID of the owner of the bot
     * @param messages      The messages the bot can say
     * @param automaticChat Can the bot talk without being triggered?
     * @param chatDelay     How long before the bot will next talk
     * @param botType       The type of the bot
     * @param mode          The mode of which the bot will perform actions (e.g walk & talk)
     * @param data          JSON object of any additional data attached to the bot
     */
    public BotData(int id, String username, String motto, String figure, String gender, String ownerName, int ownerId, String messages, boolean automaticChat, int chatDelay, BotType botType, BotMode mode, String data) {
        this.id = id;
        this.username = username;
        this.motto = motto;
        this.figure = figure;
        this.gender = gender;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.botType = botType;
        this.mode = mode;
        this.data = data;
        this.messages = (messages == null || messages.isEmpty()) ? new String[0] : JsonUtil.getInstance().fromJson(messages, String[].class);
        this.chatDelay = chatDelay;
        this.isAutomaticChat = automaticChat;
    }

    public BotData(int id, String botName, String ownerName, String botFigure, String botGender, String botMotto, BotType type) {
        this.id = id;
        this.username = botName;
        this.figure = botFigure;
        this.gender = botGender;
        this.motto = botMotto;
        this.botType = type;
    }

    @Override
    public JsonObject toJsonObject() {
        final JsonObject jsonObject = new JsonObject();
        final JsonArray jsonArray = new JsonArray();

        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("username", this.username);
        jsonObject.addProperty("motto", this.motto);
        jsonObject.addProperty("figure", this.figure);
        jsonObject.addProperty("gender", this.gender);
        jsonObject.addProperty("ownerId", this.ownerId);
        jsonObject.addProperty("ownerName", this.ownerName);
        jsonObject.addProperty("botType", this.botType.toString());
        jsonObject.addProperty("mode", this.mode.toString());
        jsonObject.addProperty("data", this.data);
        jsonObject.addProperty("chatDelay", this.chatDelay);
        jsonObject.addProperty("isAutomaticChat", this.isAutomaticChat);

        for (String message : this.messages) {
            jsonArray.add(message);
        }

        jsonObject.add("messages", jsonArray);

        return jsonObject;
    }

    /**
     * Get a random chat message from the array
     *
     * @return A random chat message from the array
     */
    @Override
    public String getRandomMessage() {
        if (this.getMessages().length > 0) {
            int index = RandomUtil.getRandomInt(0, (this.getMessages().length - 1));

            return this.stripNonAlphanumeric(this.getMessages()[index]);
        }

        return "";
    }

    /**
     * Strip non-alpha-numeric characters from a chat message
     *
     * @param msg The chat message we will filter
     * @return A filtered version of the chat message
     */
    private String stripNonAlphanumeric(String msg) {
        return msg.replace("<", "").replace(">", "");
    }

    /**
     * Save the bot data
     */
    @Override
    public void save() {
        RoomBotDao.saveData(this);
    }

    /**
     * Get the bot's ID
     *
     * @return The bot's ID
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Get the bot's name
     *
     * @return The bot's name
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Set the bot's username
     *
     * @param username The new username
     */
    @Override
    public void setUsername(String username) {
        this.username = this.stripNonAlphanumeric(username);
    }

    /**
     * Get the bot's motto
     *
     * @return The bot's motto
     */
    @Override
    public String getMotto() {
        return motto;
    }

    /**
     * Set the bot's motto
     *
     * @param motto The new motto
     */
    @Override
    public void setMotto(String motto) {
        this.motto = motto;
    }

    /**
     * Get the bot's figure
     *
     * @return The bot's figure
     */
    @Override
    public String getFigure() {
        return figure;
    }

    /**
     * Set the bot's figure
     *
     * @param figure The new figure
     */
    @Override
    public void setFigure(String figure) {
        this.figure = figure;
    }

    /**
     * Get the bot's gender
     *
     * @return The bot's gender
     */
    @Override
    public String getGender() {
        return gender;
    }

    /**
     * Set the bot's gender
     *
     * @param gender The new gender
     */
    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Get how long it is before the bot will talk
     *
     * @return Seconds until the bot will talk
     */
    @Override
    public int getChatDelay() {
        return this.chatDelay;
    }

    /**
     * Set how long it is before the bot can talk
     *
     * @param delay Seconds until the bot will talk
     */
    @Override
    public void setChatDelay(int delay) {
        this.chatDelay = delay;
    }

    /**
     * Get the bot's chat messages
     *
     * @return Bot's chat messages
     */
    @Override
    public String[] getMessages() {
        return this.messages;
    }

    /**
     * Set the bot's chat messages
     *
     * @param messages New chat messages
     */
    @Override
    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    /**
     * Can the bot talk without being triggered?
     *
     * @return Whether or not the bot can talk without being triggered
     */
    @Override
    public boolean isAutomaticChat() {
        return isAutomaticChat;
    }

    /**
     * Set whether or not the bot can talk without being triggered
     *
     * @param isAutomaticChat Whether or not the bot can talk without being triggered
     */
    @Override
    public void setAutomaticChat(boolean isAutomaticChat) {
        this.isAutomaticChat = isAutomaticChat;
    }

    /**
     * Get the name of the owner of the bot
     *
     * @return The name of the owner of the bot
     */
    @Override
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Get the ID of the owner of the bot
     *
     * @return The ID of the owner of the bot
     */
    @Override
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * Dispose the bot (Clear associated lists etc.)
     */
    @Override
    public void dispose() {
        Arrays.fill(messages, null);
    }

    @Override
    public BotType getBotType() {
        return botType;
    }

    @Override
    public BotMode getMode() {
        return mode;
    }

    @Override
    public void setMode(BotMode mode) {
        this.mode = mode;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }
}

