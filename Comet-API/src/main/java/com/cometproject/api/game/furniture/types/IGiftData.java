package com.cometproject.api.game.furniture.types;

public interface IGiftData {
    int getPageId();

    int getItemId();

    String getReceiver();

    String getMessage();

    int getSpriteId();

    int getWrappingPaper();

    int getDecorationType();

    boolean isShowUsername();

    int getSenderId();

    String getExtraData();

    void setExtraData(String extraData);
}
