package com.cometproject.server.network.messages.outgoing.quests;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.quests.Quest;
import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;

public class QuestListMessageComposer {
    public static Composer compose(Map<String, Quest> quests, Player player) {
        Composer msg = new Composer(Composers.QuestListMessageComposer);

        // TODO: show active ones first...
        msg.writeInt(quests.size());

        for(Map.Entry<String, Quest> quest : quests.entrySet()) {
            msg.writeString(quest.getValue().getCategory());
            msg.writeInt(quest.getValue().getSeriesNumber() - 1);
            msg.writeInt(QuestManager.getInstance().amountOfQuestsInCategory(quest.getValue().getCategory()));
            msg.writeInt(3); // reward type
            msg.writeInt(quest.getValue().getId());
            msg.writeBoolean(false); // started
            msg.writeString(quest.getValue().getType().getAction());
            msg.writeString(quest.getValue().getDataBit());
            msg.writeInt(0);//reward
            msg.writeString(quest.getKey());
            msg.writeInt(0); // progress
            msg.writeInt(quest.getValue().getGoalData());
            msg.writeInt(0); // "Next quest available countdown" in seconds
            msg.writeString("set_kuurna");//??
            msg.writeString("MAIN_CHAIN");//??
            msg.writeBoolean(true);//??
        }


        msg.writeBoolean(true);  // send ??
        return msg;
    }
}
