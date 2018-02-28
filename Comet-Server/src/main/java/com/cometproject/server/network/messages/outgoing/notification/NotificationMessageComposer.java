package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class NotificationMessageComposer extends MessageComposer {
    private static final String MESSAGE_KEY = "message";
    private static final String DISPLAY_KEY = "display";
    private static final String BUBBLE_DISPLAY = "BUBBLE";

    private final Map<String, String> parameters;
    private final String type;

    public NotificationMessageComposer(final String type, final Map<String, String> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    public NotificationMessageComposer(final String type, final String message) {
        this(type, new HashMap<String, String>() {{
            put(DISPLAY_KEY, BUBBLE_DISPLAY);
            put(MESSAGE_KEY, message);
        }});
    }

    public NotificationMessageComposer(final String type) {
        this(type, Maps.newHashMap());
    }

    @Override
    public short getId() {
        return Composers.RoomNotificationMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(type);

        if (parameters == null || parameters.size() == 0) {
            msg.writeInt(0);
        } else {
            msg.writeInt(parameters.size());

            for (Map.Entry<String, String> param : parameters.entrySet()) {
                msg.writeString(param.getKey());
                msg.writeString(param.getValue());
            }
        }
    }

    @Override
    public void dispose() {

    }
}
