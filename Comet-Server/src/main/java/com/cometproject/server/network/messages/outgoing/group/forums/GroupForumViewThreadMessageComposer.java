package com.cometproject.server.network.messages.outgoing.group.forums;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThreadReply;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;

public class GroupForumViewThreadMessageComposer extends MessageComposer {

    private GroupData groupData;
    private final int threadId;
    private List<ForumThreadReply> replies;
    private int start;

    public GroupForumViewThreadMessageComposer(GroupData groupData, int threadId, List<ForumThreadReply> threadReplies, int start) {
        this.groupData = groupData;
        this.threadId = threadId;
        this.replies = threadReplies;
        this.start = start;
    }

    @Override
    public short getId() {
        return Composers.ThreadDataMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.groupData.getId());
        msg.writeInt(this.threadId);
        msg.writeInt(this.start);
        msg.writeInt(this.replies.size());

        for(ForumThreadReply reply : this.replies) {
            reply.compose(msg);
        }
    }
}
