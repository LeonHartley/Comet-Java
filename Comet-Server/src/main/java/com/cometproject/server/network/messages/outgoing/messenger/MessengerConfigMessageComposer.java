package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.config.Locale;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class MessengerConfigMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.MessengerInitMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(CometSettings.messengerMaxFriends);
        msg.writeInt(300);
        msg.writeInt(800);
        // msg.writeInt(CometSettings.messengerMaxFriends);

        if (CometSettings.groupChatEnabled) {
            msg.writeInt(1);

            msg.writeInt(1);
            msg.writeString(Locale.getOrDefault("group.chats", "Group Chats"));
        } else {
            msg.writeInt(0);
        }
    }
}