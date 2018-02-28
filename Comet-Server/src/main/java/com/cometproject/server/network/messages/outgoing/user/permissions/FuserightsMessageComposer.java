package com.cometproject.server.network.messages.outgoing.user.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class FuserightsMessageComposer extends MessageComposer {
    private final boolean hasClub;
    private final int rank;

    public FuserightsMessageComposer(final boolean hasClub, final int rank) {
        this.hasClub = hasClub;
        this.rank = rank;
    }

    @Override
    public short getId() {
        return Composers.UserRightsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(2);
        msg.writeInt(this.rank);
        msg.writeBoolean(false);// Is ambassador!
    }
}
