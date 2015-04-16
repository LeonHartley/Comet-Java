package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class AccessLevelMessageComposer extends MessageComposer {
    private final int rightId;

    public AccessLevelMessageComposer(int rightId) {
        this.rightId = rightId;
    }

    @Override
    public short getId() {
        return Composers.RoomRightsLevelMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(rightId);
    }
}
