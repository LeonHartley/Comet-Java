package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;

public class ReceiveBadgeMessageComposer extends MessageComposer {

    private final int index;
    private final String badgeId;

    public ReceiveBadgeMessageComposer(int index, String badgeId) {
        this.index = index;
        this.badgeId = badgeId;
    }

    @Override
    public short getId() {
        return 0;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(index); // fk knows
        msg.writeString(badgeId);
    }
}
