package com.cometproject.server.game.catalog.types.gifts;

import com.cometproject.server.game.CometManager;

public class GiftData {
    /**
     * The page ID of the item
     */
    private int pageId;

    /**
     * The item ID of the item
     */
    private int itemId;

    /**
     * The ID of the player who sent the item
     */
    private int senderId;

    /**
     * The username of the player who will recieve the item
     */
    private String receiver;

    /**
     * The message shown in the gift
     */
    private String message;

    /**
     * The ID of the item in furnidata
     */
    private int spriteId;

    /**
     * The wrapping paper
     */
    private int wrappingPaper;

    /**
     * The box decoration type
     */
    private int decorationType;

    /**
     * Do you want to show the username in the gift?
     */
    private boolean showUsername;

    /**
     * Initialize the gift data
     * @param pageId The page ID of the item
     * @param itemId The item ID
     * @param senderId The ID of the player who sent the item
     * @param receiver The name of the user will recieve the gift
     * @param message The message that will appear in the gift
     * @param spriteId The ID of the item in furnidata
     * @param wrappingPaper
     * @param decorationType
     * @param showUsername
     */
    public GiftData(int pageId, int itemId, int senderId, String receiver, String message, int spriteId, int wrappingPaper, int decorationType, boolean showUsername) {
        this.pageId = pageId;
        this.itemId = itemId;
        this.senderId = senderId;
        this.receiver = receiver;
        this.message = message;
        this.spriteId = spriteId;
        this.wrappingPaper = wrappingPaper;
        this.decorationType = decorationType;
        this.showUsername = showUsername;
    }

    private int pos = 0;

    @Override
    public String toString() {
        if (pos != 0)
            pos = 0;

        byte[] data = new byte[13 + message.length()];

        data[pos++] = showUsername ? (byte) 1 : (byte) 0;

        writeInt(senderId, data);
        writeInt(wrappingPaper, data);
        writeInt(decorationType, data);

        for (int i = 0; i < message.getBytes().length; i++) {
            data[pos++] = message.getBytes()[i];
        }

        try {
            return new String(data, "UTF-8");
        } catch (Exception e) {
            CometManager.getLogger().error("Error while compiling GiftData", e);
            return "";
        }
    }

    private void writeInt(int value, byte[] data) {
        data[pos++] = (byte) (255 & (value >> 24));
        data[pos++] = (byte) (255 & (value >> 16));
        data[pos++] = (byte) (255 & (value >> 8));
        data[pos++] = (byte) (255 & (value));
    }

    public int getPageId() {
        return pageId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public int getSpriteId() {
        return spriteId;
    }

    public int getWrappingPaper() {
        return wrappingPaper;
    }

    public int getDecorationType() {
        return decorationType;
    }

    public boolean isShowUsername() {
        return showUsername;
    }

    public int getSenderId() {
        return senderId;
    }
}
