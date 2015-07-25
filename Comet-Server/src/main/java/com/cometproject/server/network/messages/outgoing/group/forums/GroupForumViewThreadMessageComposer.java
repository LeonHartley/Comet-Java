package com.cometproject.server.network.messages.outgoing.group.forums;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThreadReply;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GroupForumViewThreadMessageComposer extends MessageComposer {

    private GroupData groupData;
    private ForumThread forumThread;
    private int start;
    private int end;

    public GroupForumViewThreadMessageComposer(GroupData groupData, ForumThread forumThread, int start, int end) {
        this.groupData = groupData;
        this.forumThread = forumThread;
        this.start = start;
        this.end = end;
    }

    @Override
    public short getId() {
        return Composers.GroupForumViewThreadMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.groupData.getId());
        msg.writeInt(this.forumThread.getId());
        msg.writeInt(this.start);
        msg.writeInt(this.forumThread.getReplies().size());

        for(ForumThreadReply reply : this.forumThread.getReplies()) {
            final PlayerAvatar playerAvatar = PlayerManager.getInstance().getAvatarByPlayerId(reply.getAuthorId(),
                    PlayerAvatar.USERNAME_FIGURE);

            msg.writeInt(reply.getId());
            msg.writeInt(this.forumThread.getReplies().indexOf(reply));

            msg.writeInt(reply.getAuthorId());
            msg.writeString(playerAvatar.getUsername());
            msg.writeString(playerAvatar.getFigure());

            msg.writeInt((int) Comet.getTime() - reply.getAuthorTimestamp());
            msg.writeString(reply.getMessage());
            msg.writeByte(0); // state

            msg.writeInt(0); // _adminId
            msg.writeString(""); // _adminName
            msg.writeInt(0); // _adminOperationTimeAsSeccondsAgo
        }
    }
}
