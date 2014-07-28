package com.cometproject.server.game.rooms.types.misc;

public enum ChatEmotion {
    Smile(1),
    Angry(2),
    Shocked(3),
    Sad(4),
    Laugh(6);

    private int emotionId;

    ChatEmotion(int emotionId) {
        this.emotionId = emotionId;
    }

    public int getEmotionId() {
        return emotionId;
    }
}
