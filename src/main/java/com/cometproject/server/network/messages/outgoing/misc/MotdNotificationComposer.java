package com.cometproject.server.network.messages.outgoing.misc;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class MotdNotificationComposer {
    public static Composer compose() {
        if (CometSettings.messageOfTheDayEnabled) {
            return compose(CometSettings.messageOfTheDayText);
        }

        return null;
    }

    public static Composer compose(String message) {
        Composer msg = new Composer(Composers.MotdMessageComposer);

        msg.writeInt(1);
        msg.writeString(message);

        return msg;
    }
}
