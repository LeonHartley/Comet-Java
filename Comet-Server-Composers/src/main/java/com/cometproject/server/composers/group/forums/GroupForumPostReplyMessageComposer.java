package com.cometproject.server.composers.group.forums;

import com.cometproject.api.game.groups.types.components.forum.IForumThreadReply;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GroupForumPostReplyMessageComposer extends MessageComposer {
    private int groupId;
    private int threadId;
    private IForumThreadReply reply;

    public GroupForumPostReplyMessageComposer(int groupId, int threadId, IForumThreadReply reply) {
        this.groupId = groupId;
        this.threadId = threadId;
        this.reply = reply;
    }

    @Override
    public short getId() {
        return Composers.ThreadReplyMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.groupId);
        msg.writeInt(this.threadId);

        this.reply.compose(msg);
    }
}
