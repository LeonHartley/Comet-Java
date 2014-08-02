package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ManageGroupMessageComposer {
    public static Composer compose(Group group) {
        Composer msg = new Composer(Composers.ManageGroupMessageComposer);

        return msg;
    }
}
