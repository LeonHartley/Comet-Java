package com.cometproject.server.game.quests;

import com.cometproject.server.storage.queries.quests.QuestsDao;
import com.cometproject.server.utilities.Initializable;

import java.util.Map;

public class QuestManager implements Initializable {
    private static QuestManager questManagerInstance;

    private Map<String, Quest> quests;

    public QuestManager() {

    }

    @Override
    public void initialize() {
        this.quests = QuestsDao.getAllQuests();
    }

    public static QuestManager getInstance() {
        if(questManagerInstance == null) {
            questManagerInstance = new QuestManager();
        }

        return questManagerInstance;
    }

    public int amountOfQuestsInCategory(String category) {
        int count = 0;

        for(Quest quest : this.quests.values()) {
            if(quest.getCategory().equals(category)) {
                count++;
            }
        }

        return count;
    }

    public Map<String, Quest> getQuests() {
        return quests;
    }
}
