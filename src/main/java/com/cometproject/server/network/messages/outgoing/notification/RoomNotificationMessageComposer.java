package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.HashMap;
import java.util.Map;

public class RoomNotificationMessageComposer {
    private static final String MESSAGE_KEY = "message";
    private static final String DISPLAY_KEY = "display";

    private static final String BUBBLE_DISPLAY = "BUBBLE";
    public static Composer compose(String type, Map<String, String> parameters) {
        Composer msg = new Composer(Composers.RoomNotificationMessageComposer);

        msg.writeString(type);

        if(parameters == null) {
            msg.writeInt(0);
        } else {
            msg.writeInt(parameters.size());

            for(Map.Entry<String, String> param : parameters.entrySet()) {
                msg.writeString(param.getKey());
                msg.writeString(param.getValue());
            }
        }

        return msg;
    }

    public static Composer compose(String type) {
        return RoomNotificationMessageComposer.compose(type, null);
    }

    public static Composer bubble(String type, String message) {
        return RoomNotificationMessageComposer.compose(type, new HashMap<String, String>() {{
            put(DISPLAY_KEY, BUBBLE_DISPLAY);
            put(MESSAGE_KEY, message);
        }});
    }
}
