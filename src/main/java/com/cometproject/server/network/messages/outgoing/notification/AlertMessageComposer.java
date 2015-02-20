package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class AlertMessageComposer {
    public static Composer compose(String message, String link) {
        Composer msg = new Composer(Composers.AlertNotificationMessageComposer);

        msg.writeString(message);
        msg.writeString(link);

        return msg;
    }

    public static Composer compose(String message) {
        return compose(message, "");
    }
}
