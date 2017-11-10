package com.cometproject.api.game.quests;

import java.util.Map;

public interface IQuestService {

    void loadQuests();

    IQuest getById(int questId);

    int getAmountOfQuestsInCategory(String category);

    IQuest getNextQuestInSeries(IQuest lastQuest);

    Map<String, IQuest> getQuests();
}
