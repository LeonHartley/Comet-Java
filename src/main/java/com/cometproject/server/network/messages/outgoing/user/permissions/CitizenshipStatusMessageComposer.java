package com.cometproject.server.network.messages.outgoing.user.permissions;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class CitizenshipStatusMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.CitizenshipStatusMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeString("citizenship");
        msg.writeInt(4);
        msg.writeInt(4);

    }
}
