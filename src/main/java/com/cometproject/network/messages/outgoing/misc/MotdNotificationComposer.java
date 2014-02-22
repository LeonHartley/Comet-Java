package com.cometproject.network.messages.outgoing.misc;

import com.cometproject.config.CometSettings;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class MotdNotificationComposer {
    public static Composer compose() {
        if(CometSettings.messageOfTheDayEnabled) {
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
