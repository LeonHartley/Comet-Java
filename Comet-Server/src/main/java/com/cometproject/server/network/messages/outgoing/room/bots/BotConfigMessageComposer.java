package com.cometproject.server.network.messages.outgoing.room.bots;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class BotConfigMessageComposer extends MessageComposer {
    private final int botId;
    private final int skill;
    private final String message;

    public BotConfigMessageComposer(final int botId, final int skill, final String message) {
        this.botId = botId;
        this.skill = skill;
        this.message = message;
    }

    @Override
    public short getId() {
        return Composers.BotSpeechListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(botId);
        msg.writeInt(skill);
        msg.writeString(message);
    }
}
