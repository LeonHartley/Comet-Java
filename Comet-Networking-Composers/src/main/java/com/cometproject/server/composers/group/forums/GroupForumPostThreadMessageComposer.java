package com.cometproject.server.composers.group.forums;

import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GroupForumPostThreadMessageComposer extends MessageComposer {
    private int groupId;
    private IForumThread forumThread;

    public GroupForumPostThreadMessageComposer(int groupId, IForumThread forumThread) {
        this.groupId = groupId;
        this.forumThread = forumThread;
    }

    @Override
    public short getId() {
        return Composers.ThreadCreatedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(groupId);

        forumThread.compose(msg);
    }
}
