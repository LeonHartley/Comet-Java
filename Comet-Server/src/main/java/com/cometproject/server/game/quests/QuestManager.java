package com.cometproject.server.game.quests;

import com.cometproject.api.game.quests.IQuest;
import com.cometproject.api.game.quests.IQuestService;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.storage.queries.quests.QuestsDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class QuestManager implements Initialisable, IQuestService {
    private static final Logger log = LogManager.getLogger(QuestManager.class.getName());

    private static QuestManager questManagerInstance;

    private Map<String, IQuest> quests;

    public QuestManager() {

    }

    public static QuestManager getInstance() {
        if (questManagerInstance == null) {
            questManagerInstance = new QuestManager();
        }

        return questManagerInstance;
    }

    @Override
    public void initialize() {
        this.loadQuests();
    }

    @Override
    public void loadQuests() {
        if (this.quests != null) {
            this.quests.clear();
        }

        this.quests = QuestsDao.getAllQuests();
        log.info("Loaded {} quests", this.quests.size());
        log.info("QuestManager initialized");
    }

    @Override
    public IQuest getById(int questId) {
        for (IQuest quest : this.quests.values()) {
            if (quest.getId() == questId)
                return quest;
        }

        return null;
    }

    @Override
    public int getAmountOfQuestsInCategory(String category) {
        int count = 0;

        for (IQuest quest : this.quests.values()) {
            if (quest.getCategory().equals(category)) {
                count++;
            }
        }

        return count;
    }

    @Override
    public IQuest getNextQuestInSeries(IQuest lastQuest) {
        for (IQuest quest : this.quests.values()) {
            if (quest.getCategory().equals(lastQuest.getCategory()) &&
                    quest.getSeriesNumber() == (lastQuest.getSeriesNumber() + 1)) {
                return quest;
            }
        }

        return null;
    }

    @Override
    public Map<String, IQuest> getQuests() {
        return quests;
    }
}
