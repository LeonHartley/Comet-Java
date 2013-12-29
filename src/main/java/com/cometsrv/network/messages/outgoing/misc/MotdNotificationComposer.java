package com.cometsrv.network.messages.outgoing.misc;

import com.cometsrv.config.CometSettings;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

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
