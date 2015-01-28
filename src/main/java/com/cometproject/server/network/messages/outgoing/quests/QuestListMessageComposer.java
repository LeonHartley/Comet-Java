package com.cometproject.server.network.messages.outgoing.quests;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.quests.Quest;
import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.utilities.Counter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class QuestListMessageComposer {
    public static Composer compose(Map<String, Quest> quests, Player player) {
        Composer msg = new Composer(Composers.QuestListMessageComposer);

        Map<String, Counter> categoryCounters = Maps.newHashMap();

        List<Quest> activeQuests = Lists.newArrayList();
        List<Quest> inactiveQuests = Lists.newArrayList();

        try {
            for (Quest quest : quests.values()) {
                if(!categoryCounters.containsKey(quest.getCategory())) {
                    categoryCounters.put(quest.getCategory(), new Counter(1));
                }

                if(quest.getSeriesNumber() >= categoryCounters.get(quest.getCategory()).get()) {
                    if (player.getQuests().hasStartedQuest(quest.getId())) {
                        activeQuests.add(quest);
                    } else {
                        inactiveQuests.add(quest);
                    }
                }
            }

            msg.writeInt(activeQuests.size() + inactiveQuests.size());

            for (Quest activeQuest : activeQuests) {
                composeQuest(activeQuest, msg);
            }

            for (Quest inactiveQuest : activeQuests) {
                composeQuest(inactiveQuest, msg);
            }

            msg.writeBoolean(true);  // send ??

            return msg;
            
        } finally {
            categoryCounters.clear();

            activeQuests.clear();
            inactiveQuests.clear();
        }
    }
    
    private static void composeQuest(Quest quest, Composer msg) {
        msg.writeString(quest.getCategory());
        msg.writeInt(quest.getSeriesNumber() - 1);
        msg.writeInt(QuestManager.getInstance().amountOfQuestsInCategory(quest.getCategory()));
        msg.writeInt(3); // reward type
        msg.writeInt(quest.getId());
        msg.writeBoolean(false); // started
        msg.writeString(quest.getType().getAction());
        msg.writeString(quest.getDataBit());
        msg.writeInt(0);//reward
        msg.writeString(quest.getName());
        msg.writeInt(0); // progress
        msg.writeInt(quest.getGoalData()); // total steps to get goal
        msg.writeInt(0); // sort order
        msg.writeString("set_kuurna");
        msg.writeString("MAIN_CHAIN");
        msg.writeBoolean(true);// easy
    }
}
