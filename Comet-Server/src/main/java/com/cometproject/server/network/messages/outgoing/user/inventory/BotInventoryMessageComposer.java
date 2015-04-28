package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.game.players.components.types.inventory.InventoryBot;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;


public class BotInventoryMessageComposer extends MessageComposer {

    private final Map<Integer, InventoryBot> bots;

    public BotInventoryMessageComposer(final Map<Integer, InventoryBot> bots) {
        this.bots = bots;
    }

    @Override
    public short getId() {
        return Composers.BotInventoryMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(bots.size());

        for (Map.Entry<Integer, InventoryBot> bot : bots.entrySet()) {
            msg.writeInt(bot.getKey());
            msg.writeString(bot.getValue().getName());
            msg.writeString(bot.getValue().getMotto());
            msg.writeString(bot.getValue().getGender());
            msg.writeString(bot.getValue().getFigure());
        }
    }
}
