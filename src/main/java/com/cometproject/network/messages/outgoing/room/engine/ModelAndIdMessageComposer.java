package com.cometproject.network.messages.outgoing.room.engine;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class ModelAndIdMessageComposer {
    public static Composer compose(String modelId, int userId) {
        Composer msg = new Composer(Composers.ModelAndIdMessageComposer);

        msg.writeString(modelId);
        msg.writeInt(userId);

        return msg;
    }
}
