package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;

public class BotInventoryMessageComposer {
    public static Composer compose(Map<Integer, InventoryBot> bots) {
        Composer msg = new Composer(Composers.BotInventoryMessageComposer);

        msg.writeInt(bots.size());

        for(Map.Entry<Integer, InventoryBot> bot : bots.entrySet()) {
            msg.writeInt(bot.getKey());
            msg.writeString(bot.getValue().getName());
            msg.writeString(bot.getValue().getMotto());
            msg.writeString(bot.getValue().getGender());
            msg.writeString(bot.getValue().getFigure());
        }

        return msg;
    }
}
