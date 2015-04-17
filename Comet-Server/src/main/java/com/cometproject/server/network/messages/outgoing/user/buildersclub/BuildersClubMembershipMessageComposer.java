package com.cometproject.server.network.messages.outgoing.user.buildersclub;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class BuildersClubMembershipMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.BuildersClubMembershipMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(999999999);
        msg.writeInt(100);
        msg.writeInt(2);
    }
}
