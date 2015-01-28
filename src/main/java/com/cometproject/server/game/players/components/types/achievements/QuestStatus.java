package com.cometproject.server.game.players.components.types.achievements;

public class QuestStatus {
    private final int questId;
    private int progress;

    public QuestStatus(int questId, int progress) {
        this.questId = questId;
        this.progress = progress;
    }

    public int getQuestId() {
        return this.questId;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
