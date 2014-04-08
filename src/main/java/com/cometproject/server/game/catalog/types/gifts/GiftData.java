package com.cometproject.server.game.catalog.types.gifts;

import com.cometproject.server.game.GameEngine;

public class GiftData {
    private int pageId;
    private int itemId;
    private String sendingUser;
    private String message;
    private int spriteId;
    private int wrappingPaper;
    private int decorationType;
    private boolean showUsername;

    public GiftData(int pageId, int itemId, String sendingUser, String message, int spriteId, int wrappingPaper, int decorationType, boolean showUsername) {
        this.pageId = pageId;
        this.itemId = itemId;
        this.sendingUser = sendingUser;
        this.message = message;
        this.spriteId = spriteId;
        this.wrappingPaper = wrappingPaper;
        this.decorationType = decorationType;
        this.showUsername = showUsername;
    }

    private int pos = 0;

    public String toString(int senderId) {
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
            GameEngine.getLogger().error("Error while compiling GiftData", e);
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

    public String getSendingUser() {
        return sendingUser;
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
}
