package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


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
        msg.writeInt(CometSettings.messengerMaxFriends);
        msg.writeInt(0);
    }
}
