package com.cometproject.server.network.messages.outgoing.quests;

import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.quests.IQuest;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class QuestStartedMessageComposer extends MessageComposer {
    private final IPlayer player;
    private final IQuest quest;

    public QuestStartedMessageComposer(IQuest quest, IPlayer player) {
        this.quest = quest;
        this.player = player;
    }

    @Override
    public short getId() {
        return Composers.QuestStartedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        this.quest.compose(player, msg);
    }
}
