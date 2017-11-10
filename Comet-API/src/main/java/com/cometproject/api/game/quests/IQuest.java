package com.cometproject.api.game.quests;

import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.networking.messages.IComposer;

public interface IQuest {

    void compose(IPlayer player, IComposer msg);

    QuestType getType();

    int getId();

    String getName();

    String getCategory();

    int getSeriesNumber();

    int getGoalType();

    int getGoalData();

    int getReward();

    QuestReward getRewardType();

    String getDataBit();

    String getBadgeId();
}
