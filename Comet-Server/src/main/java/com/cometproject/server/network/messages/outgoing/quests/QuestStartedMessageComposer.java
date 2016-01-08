package com.cometproject.server.network.messages.outgoing.quests;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.quests.types.Quest;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class QuestStartedMessageComposer extends MessageComposer {
    private final Player player;
    private final Quest quest;

    public QuestStartedMessageComposer(Quest quest, Player player) {
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
