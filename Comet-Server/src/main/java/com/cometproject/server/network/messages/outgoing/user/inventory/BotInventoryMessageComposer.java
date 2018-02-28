package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.game.bots.IBotData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Map;


public class BotInventoryMessageComposer extends MessageComposer {

    private final Map<Integer, IBotData> bots;

    public BotInventoryMessageComposer(final Map<Integer, IBotData> bots) {
        this.bots = bots;
    }

    public BotInventoryMessageComposer() {
        this.bots = null;
    }

    @Override
    public short getId() {
        return Composers.BotInventoryMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (this.bots == null) {
            msg.writeInt(0);
            return;
        }

        msg.writeInt(bots.size());

        for (Map.Entry<Integer, IBotData> bot : bots.entrySet()) {
            msg.writeInt(bot.getKey());
            msg.writeString(bot.getValue().getUsername());
            msg.writeString(bot.getValue().getMotto());
            msg.writeString(bot.getValue().getGender());
            msg.writeString(bot.getValue().getFigure());
        }
    }
}
