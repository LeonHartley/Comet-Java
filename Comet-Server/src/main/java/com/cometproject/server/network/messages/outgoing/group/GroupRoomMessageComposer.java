package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class GroupRoomMessageComposer extends MessageComposer {
    private final int roomId;
    private final int groupId;

    public GroupRoomMessageComposer(final int roomId, final int groupId) {
        this.roomId = roomId;
        this.groupId = groupId;
    }

    @Override
    public short getId() {
        return Composers.NewGroupInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomId);
        msg.writeInt(groupId);
    }
}
