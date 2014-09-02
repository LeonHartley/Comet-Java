package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class GroupDataMessageComposer {
    public static Composer compose(List<Integer> groups) {
        Composer msg = new Composer(Composers.GroupDataMessageComposer);

        msg.writeInt(groups.size());

        for(Integer groupId : groups) {

        }

        return msg;
    }
}
