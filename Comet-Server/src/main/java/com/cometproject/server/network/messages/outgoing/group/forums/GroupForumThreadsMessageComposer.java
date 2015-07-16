package com.cometproject.server.network.messages.outgoing.group.forums;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class GroupForumThreadsMessageComposer extends MessageComposer{

    private final int groupId;

    public GroupForumThreadsMessageComposer(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public short getId() {
        return Composers.GroupForumThreadsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.groupId);
        msg.writeInt(0);
        msg.writeInt(0); // count
    }
}
