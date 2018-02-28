package com.cometproject.server.network.messages.outgoing.room.bots;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


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
        return Composers.OpenBotActionMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(botId);
        msg.writeInt(skill);
        msg.writeString(message);
    }
}
