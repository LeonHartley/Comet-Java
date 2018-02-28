package com.cometproject.server.network.messages.outgoing.quests;

import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.quests.IQuest;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class QuestListMessageComposer extends MessageComposer {
    private final Map<String, IQuest> quests;
    private final IPlayer player;
    private boolean isWindow;

    public QuestListMessageComposer(final Map<String, IQuest> quests, IPlayer player, boolean isWindow) {
        this.quests = quests;
        this.player = player;
        this.isWindow = isWindow;
    }

    @Override
    public short getId() {
        return Composers.QuestListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        Map<String, IQuest> categoryCounters = Maps.newHashMap();

        List<IQuest> activeQuests = Lists.newArrayList();
        List<IQuest> inactiveQuests = Lists.newArrayList();

        try {
            for (IQuest quest : this.quests.values()) {
                if (categoryCounters.containsKey(quest.getCategory())) {
                    if (!this.player.getQuests().hasCompletedQuest(quest.getId())) {
                        if (!this.player.getQuests().hasCompletedQuest(categoryCounters.get(quest.getCategory()).getId())) {
                            if (categoryCounters.get(quest.getCategory()).getSeriesNumber() > quest.getSeriesNumber()) {
                                categoryCounters.replace(quest.getCategory(), quest);
                            }
                        } else {
                            if (categoryCounters.get(quest.getCategory()).getSeriesNumber() < quest.getSeriesNumber()) {
                                categoryCounters.replace(quest.getCategory(), quest);
                            }
                        }
                    } else {
                        if (quest.getSeriesNumber() > categoryCounters.get(quest.getCategory()).getSeriesNumber()) {
                            categoryCounters.replace(quest.getCategory(), quest);
                        }
                    }
                } else {
                    categoryCounters.put(quest.getCategory(), quest);
                }
            }

            for (IQuest quest : categoryCounters.values()) {
                if (this.player.getQuests().hasCompletedQuest(quest.getId())) {
                    inactiveQuests.add(quest);
                } else {
                    activeQuests.add(quest);
                }
            }

            msg.writeInt(activeQuests.size() + inactiveQuests.size());

            for (IQuest activeQuest : activeQuests) {
                composeQuest(activeQuest.getCategory(), activeQuest, msg);
            }

            for (IQuest inactiveQuest : inactiveQuests) {
                composeQuest(inactiveQuest.getCategory(), null, msg);
            }

            msg.writeBoolean(this.isWindow);  // send ??
        } finally {
            categoryCounters.clear();

            inactiveQuests.clear();
            activeQuests.clear();
        }
    }

    private void composeQuest(final String category, final IQuest quest, final IComposer msg) {
        int amountInCategory = QuestManager.getInstance().getAmountOfQuestsInCategory(category);

        if (quest == null) {
            msg.writeString(category);
            msg.writeInt(amountInCategory);
            msg.writeInt(amountInCategory);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeBoolean(false);
            msg.writeString("");
            msg.writeString("");
            msg.writeInt(0);
            msg.writeString("");
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeString("");
            msg.writeString("");
            msg.writeBoolean(true);// easy
            return;
        }

        quest.compose(this.player, msg);
    }
}
