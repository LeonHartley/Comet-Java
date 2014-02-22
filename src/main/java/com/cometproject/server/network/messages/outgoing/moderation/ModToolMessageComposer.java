package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ModToolMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.ModToolMessageComposer);

        msg.writeInt(0);

        msg.writeInt(GameEngine.getModeration().getUserPresets().size());

        for(String preset : GameEngine.getModeration().getUserPresets()) {
            msg.writeString(preset);
        }

        msg.writeInt(0);
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeBoolean(true);

        msg.writeInt(0);

        msg.writeInt(GameEngine.getModeration().getRoomPresets().size());

        for(String preset : GameEngine.getModeration().getRoomPresets()) {
            msg.writeString(preset);
        }

        return msg;
    }
}
