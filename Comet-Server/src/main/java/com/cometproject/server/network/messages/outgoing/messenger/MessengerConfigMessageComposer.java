package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class MessengerConfigMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.LoadFriendsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(CometSettings.maxFriends);
        msg.writeInt(300);
        msg.writeInt(800);
        msg.writeInt(CometSettings.maxFriends);
        msg.writeInt(0);
    }
}
