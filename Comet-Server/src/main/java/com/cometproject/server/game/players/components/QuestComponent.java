package com.cometproject.server.game.players.components;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.game.quests.Quest;
import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.storage.queries.quests.PlayerQuestsDao;

import java.util.Map;

public class QuestComponent implements PlayerComponent {
    private Player player;
    private Map<Integer, Integer> questProgression;

    public QuestComponent(Player player) {
        this.player = player;

        this.loadQuestProgression();
    }

    private void loadQuestProgression() {
        this.questProgression = PlayerQuestsDao.getQuestProgression(this.getPlayer().getId());
    }

    public boolean hasStartedQuest(int questId) {
        return this.questProgression.containsKey(questId);
    }

    public boolean hasCompletedQuest(int questId) {
        final Quest quest = QuestManager.getInstance().getById(questId);

        if(quest == null) return false;

        if(this.questProgression.containsKey(questId)) {
            if(this.questProgression.get(questId) == quest.getGoalData()) {
                return true;
            }
        }

        return false;
    }

    public void startQuest(Quest quest) {
        if(this.questProgression.containsKey(quest.getId())) {
            // We've already started this quest
            return;
        }

        this.questProgression.put(quest.getId(), 0);
        PlayerQuestsDao.saveProgression(true, this.player.getId(), quest.getId(), 0);
    }

    public void progressQuest(int questId, int newProgressValue) {
        boolean insertRequired = false;

        if(this.questProgression.containsKey(questId)) {
            this.questProgression.replace(questId, newProgressValue);
        } else {
            this.questProgression.put(questId, newProgressValue);
            insertRequired = true;
        }

        PlayerQuestsDao.saveProgression(insertRequired, this.player.getId(), questId, newProgressValue);
    }

    public int getProgress(int quest) {
        if(this.questProgression.containsKey(quest)) {
            return this.questProgression.get(quest);
        }

        return 0;
    }

    @Override
    public void dispose() {
        this.questProgression.clear();
        this.questProgression = null;

        this.player = null;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }
}
