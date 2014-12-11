package com.cometproject.server.network.messages.outgoing.user.buildersclub;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class BuildersClubMembershipMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.BuildersClubMembershipMessageComposer);

        msg.writeInt(999999999);
        msg.writeInt(100);
        msg.writeInt(2);

        return msg;
    }
}
