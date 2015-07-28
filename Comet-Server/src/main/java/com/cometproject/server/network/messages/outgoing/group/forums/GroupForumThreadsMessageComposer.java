package com.cometproject.server.network.messages.outgoing.group.forums;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class GroupForumThreadsMessageComposer extends MessageComposer {

    private final Group group;

    public GroupForumThreadsMessageComposer(Group group) {
        this.group = group;
    }

    @Override
    public short getId() {
        return Composers.GroupForumThreadsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.group.getId());
        msg.writeInt(0); // start index.

        int pinnedThreads = this.group.getForumComponent().getPinnedThreads().size();
        int threadCount = pinnedThreads + (this.group.getForumComponent().getForumThreads().size() - pinnedThreads);

        msg.writeInt(threadCount); // count

        for(int pinnedThread : this.group.getForumComponent().getPinnedThreads()) {
            ForumThread forumThread = this.group.getForumComponent().getForumThreads().get(pinnedThread);

            forumThread.compose(msg);
        }

        for (ForumThread forumThread : this.group.getForumComponent().getForumThreads().values()) {
            if(forumThread.isPinned()) continue;

            forumThread.compose(msg);
        }
    }
}