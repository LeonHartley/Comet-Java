package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class InviteFriendMessageComposer extends MessageComposer {
    private final String message;
    private final int fromId;

    public InviteFriendMessageComposer(final String message, final int fromId) {
        this.message = message;
        this.fromId = fromId;
    }

    @Override
    public short getId() {
        return Composers.RoomInviteMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(fromId);
        msg.writeString(message);
    }
}
