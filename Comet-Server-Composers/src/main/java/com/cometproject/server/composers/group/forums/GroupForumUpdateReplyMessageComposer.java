package com.cometproject.server.composers.group.forums;

import com.cometproject.api.game.groups.types.components.forum.IForumThreadReply;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GroupForumUpdateReplyMessageComposer extends MessageComposer {

    private final int groupId;
    private final int threadId;
    private final IForumThreadReply reply;

    public GroupForumUpdateReplyMessageComposer(IForumThreadReply reply, int threadId, int groupId) {
        this.reply = reply;
        this.threadId = threadId;
        this.groupId = groupId;
    }

    @Override
    public short getId() {
        return Composers.PostUpdatedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.groupId);
        msg.writeInt(this.threadId);

        this.reply.compose(msg);
    }
}
