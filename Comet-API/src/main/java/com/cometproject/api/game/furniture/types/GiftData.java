package com.cometproject.api.game.furniture.types;

public class GiftData {
    public static final String EXTRA_DATA_HEADER = "GIFTv2::##";

    private final int definitionId;
    private final int senderId;
    private final String receiver;
    private final String message;
    private final int spriteId;
    private final int wrappingPaper;
    private final int decorationType;
    private final boolean showUsername;
    private String extraData;

    public GiftData(int itemDefinitionId, int senderId, String receiver, String message, int spriteId, int wrappingPaper, int decorationType, boolean showUsername, String extraData) {
        this.definitionId = itemDefinitionId;
        this.senderId = senderId;
        this.receiver = receiver;
        this.message = message;
        this.spriteId = spriteId;
        this.wrappingPaper = wrappingPaper;
        this.decorationType = decorationType;
        this.showUsername = showUsername;
        this.extraData = extraData;
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

    public boolean showUsername() {
        return showUsername;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public int getDefinitionId() {
        return definitionId;
    }
}
