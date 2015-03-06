package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class MotdNotificationComposer extends MessageComposer {
    private final String message;

    public MotdNotificationComposer(final String message) {
        this.message = message;
    }

    public MotdNotificationComposer() {
        this(CometSettings.messageOfTheDayText);
    }

    @Override
    public short getId() {
        return Composers.MOTDNotificationMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(1);
        msg.writeString(message);
    }
}
