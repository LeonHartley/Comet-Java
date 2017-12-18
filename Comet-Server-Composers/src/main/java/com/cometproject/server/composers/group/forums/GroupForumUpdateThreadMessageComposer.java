package com.cometproject.server.composers.group.forums;

import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GroupForumUpdateThreadMessageComposer extends MessageComposer {
    private int groupId;
    private IForumThread forumThread;

    public GroupForumUpdateThreadMessageComposer(int groupId, IForumThread forumThread) {
        this.groupId = groupId;
        this.forumThread = forumThread;
    }

    @Override
    public short getId() {
        return Composers.ThreadUpdatedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.groupId);

        this.forumThread.compose(msg);
    }
}
