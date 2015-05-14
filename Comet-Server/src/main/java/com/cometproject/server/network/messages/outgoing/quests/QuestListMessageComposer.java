package com.cometproject.server.network.messages.outgoing.quests;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.quests.Quest;
import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class QuestListMessageComposer extends MessageComposer {
    private final Map<String, Quest> quests;
    private final Player player;

    public QuestListMessageComposer(final Map<String, Quest> quests, Player player) {
        this.quests = quests;
        this.player = player;
    }

    @Override
    public short getId() {
        return Composers.QuestListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        Map<String, Quest> categoryCounters = Maps.newHashMap();

        List<Quest> activeQuests = Lists.newArrayList();
        List<Quest> inactiveQuests = Lists.newArrayList();

        try {
            for (Quest quest : this.quests.values()) {
                boolean hasCompletedQuest = this.player.getQuests().hasCompletedQuest(quest.getId());

                if(hasCompletedQuest) {
                    continue;
                }

                if(categoryCounters.containsKey(quest.getCategory())) {
                    if(categoryCounters.get(quest.getCategory()).getSeriesNumber() > quest.getSeriesNumber()) {
                        categoryCounters.replace(quest.getCategory(), quest);
                    }
                } else {
                    categoryCounters.put(quest.getCategory(), quest);
                }
            }

            for(Quest quest : categoryCounters.values()) {
                if(this.player.getQuests().hasStartedQuest(quest.getId())) {
                    activeQuests.add(quest);
                } else {
                    inactiveQuests.add(quest);
                }
            }

            msg.writeInt(activeQuests.size() + inactiveQuests.size());

            for (Quest activeQuest : activeQuests) {
                composeQuest(activeQuest, msg);
            }

            for (Quest inactiveQuest : inactiveQuests) {
                composeQuest(inactiveQuest, msg);
            }

            msg.writeBoolean(true);  // send ??
        } finally {
            categoryCounters.clear();

            activeQuests.clear();
            inactiveQuests.clear();
        }
    }

    private void composeQuest(final Quest quest, final IComposer msg) {
        msg.writeString(quest.getCategory());
        msg.writeInt(quest.getSeriesNumber() - 1);
        msg.writeInt(QuestManager.getInstance().amountOfQuestsInCategory(quest.getCategory()) - 1);
        msg.writeInt(3); // reward type (pixels)
        msg.writeInt(quest.getId());
        msg.writeBoolean(false); // started
        msg.writeString(quest.getType().getAction());
        msg.writeString(quest.getDataBit());
        msg.writeInt(0);//reward
        msg.writeString(quest.getName());
        msg.writeInt(this.player.getQuests().getProgress(quest.getId())); // progress
        msg.writeInt(quest.getGoalData()); // total steps to get goal
        msg.writeInt(0); // sort order
        msg.writeString("set_kuurna");
        msg.writeString("MAIN_CHAIN");
        msg.writeBoolean(true);// easy
    }
}
