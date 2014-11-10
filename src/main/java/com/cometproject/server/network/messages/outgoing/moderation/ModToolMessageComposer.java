package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ModToolMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.LoadModerationToolMessageComposer);

        msg.writeInt(0);

        msg.writeInt(CometManager.getModeration().getUserPresets().size());

        for (String preset : CometManager.getModeration().getUserPresets()) {
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

        msg.writeInt(CometManager.getModeration().getRoomPresets().size());

        for (String preset : CometManager.getModeration().getRoomPresets()) {
            msg.writeString(preset);
        }

        return msg;
    }
}
