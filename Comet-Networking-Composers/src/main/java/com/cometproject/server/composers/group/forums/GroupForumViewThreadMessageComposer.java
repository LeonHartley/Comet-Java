package com.cometproject.server.composers.group.forums;

import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.forum.IForumThreadReply;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;

public class GroupForumViewThreadMessageComposer extends MessageComposer {

    private IGroupData groupData;
    private final int threadId;
    private List<IForumThreadReply> replies;
    private int start;

    public GroupForumViewThreadMessageComposer(IGroupData groupData, int threadId, List<IForumThreadReply> threadReplies, int start) {
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

        for(IForumThreadReply reply : this.replies) {
            reply.compose(msg);
        }
    }
}
