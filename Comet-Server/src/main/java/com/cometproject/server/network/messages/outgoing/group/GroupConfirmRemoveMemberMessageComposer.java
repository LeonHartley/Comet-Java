package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GroupConfirmRemoveMemberMessageComposer extends MessageComposer {
    private final int playerId;
    private final int furniCount;

    public GroupConfirmRemoveMemberMessageComposer(int playerId, int furniCount) {
        this.playerId = playerId;
        this.furniCount = furniCount;
    }

    public short getId() {
        return Composers.GroupConfirmRemoveMemberMessageComposer;
    }

    public void compose(IComposer msg) {
        msg.writeInt(this.playerId);
        msg.writeInt(this.furniCount);
    }
}
