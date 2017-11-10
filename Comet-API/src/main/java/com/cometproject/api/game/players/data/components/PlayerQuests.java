package com.cometproject.api.game.players.data.components;

import com.cometproject.api.game.quests.IQuest;
import com.cometproject.api.game.quests.QuestType;

public interface PlayerQuests {

    void loadQuestProgression();

    boolean hasStartedQuest(int questId);

    boolean hasCompletedQuest(int questId);

    void startQuest(IQuest quest);

    void cancelQuest(int questId);

    void progressQuest(QuestType type);

    void progressQuest(QuestType type, int data);

    int getProgress(int quest);
}
