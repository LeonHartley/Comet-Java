package com.cometproject.server.composers.group.forums;

import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;


public class GroupForumThreadsMessageComposer extends MessageComposer {

    private final int groupId;
    private final List<IForumThread> threads;
    private final int start;

    public GroupForumThreadsMessageComposer(int groupId, List<IForumThread> threads, int start) {
        this.groupId = groupId;
        this.threads = threads;

        this.start = start;
}

    @Override
    public short getId() {
        return Composers.ThreadsListDataMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.groupId);
        msg.writeInt(this.start); // start index.

        msg.writeInt(this.threads.size());

        for(IForumThread forumThread : this.threads) {
            forumThread.compose(msg);
        }
    }

    @Override
    public void dispose() {
        this.threads.clear();
    }
}