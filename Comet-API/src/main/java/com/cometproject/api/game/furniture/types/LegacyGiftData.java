package com.cometproject.api.game.furniture.types;

public class LegacyGiftData extends GiftData {
    public static final String EXTRA_DATA_HEADER = "GIFT::##";

    private final int itemId;
    private final int pageId;

    public LegacyGiftData(int pageId, int itemId, int senderId, String receiver, String message, int spriteId, int wrappingPaper, int decorationType, boolean showUsername, String extraData) {
        super(0, senderId, receiver, message, spriteId, wrappingPaper, decorationType, showUsername, extraData);

        this.itemId = itemId;
        this.pageId = pageId;
    }

    public GiftData upgrade(int itemDefinitionId) {
        return new GiftData(itemDefinitionId, this.getSenderId(), this.getReceiver(), this.getMessage(), this.getSpriteId(), this.getWrappingPaper(),  this.getDecorationType(), this.showUsername(), this.getExtraData());
    }

    public int getItemId() {
        return itemId;
    }

    public int getPageId() {
        return pageId;
    }
}
