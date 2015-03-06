package com.cometproject.server.network.messages.outgoing.user.permissions;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


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
    public void compose(Composer msg) {
        msg.writeInt(hasClub ? 2 : 0);
        msg.writeInt(rank);
        msg.writeBoolean(false);// Is ambassador!
    }
}
