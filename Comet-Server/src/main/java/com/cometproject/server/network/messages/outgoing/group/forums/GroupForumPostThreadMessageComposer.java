package com.cometproject.server.network.messages.outgoing.group.forums;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GroupForumPostThreadMessageComposer extends MessageComposer {
    private int groupId;
    private ForumThread forumThread;

    public GroupForumPostThreadMessageComposer(int groupId, ForumThread forumThread) {
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
