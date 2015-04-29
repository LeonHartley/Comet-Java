package com.cometproject.server.network.messages.outgoing.user.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;


public class FuserightsMessageComposer extends MessageComposer {
    private final boolean hasClub;
    private final int rank;

    public FuserightsMessageComposer(final boolean hasClub, final int rank) {
        this.hasClub = hasClub;
        this.rank = rank;
    }

    @Override
    public short getId() {
        return Composers.UserClubRightsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(hasClub ? 2 : 0);
        msg.writeInt(rank);
        msg.writeBoolean(false);// Is ambassador!
    }
}
