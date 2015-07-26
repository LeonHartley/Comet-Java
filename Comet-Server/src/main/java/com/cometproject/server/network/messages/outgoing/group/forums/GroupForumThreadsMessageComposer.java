package com.cometproject.server.network.messages.outgoing.group.forums;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerAvatar;
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
        msg.writeInt(0);
        msg.writeInt(this.group.getForumComponent().getForumThreads().size()); // count

        for (ForumThread forumThread : this.group.getForumComponent().getForumThreads().values()) {
            msg.writeInt(forumThread.getId());

            final PlayerAvatar authorAvatar = PlayerManager.getInstance().getAvatarByPlayerId(forumThread.getAuthorId(), PlayerAvatar.USERNAME_FIGURE);

            msg.writeInt(authorAvatar == null ? 0 : authorAvatar.getId());
            msg.writeString(authorAvatar == null ? "Unknown Player" : authorAvatar.getUsername());

            msg.writeString(forumThread.getTitle());
            msg.writeBoolean(false); // stickied.
            msg.writeBoolean(forumThread.isLocked());
            msg.writeInt((int) Comet.getTime() - forumThread.getAuthorTimestamp()); // create time
            msg.writeInt(forumThread.getReplies().size()); // messages
            msg.writeInt(0); // unread messages
            msg.writeInt(forumThread.getMostRecentPost().getId()); // last message id

            final PlayerAvatar replyAuthor = PlayerManager.getInstance().getAvatarByPlayerId(forumThread.getMostRecentPost().getAuthorId(), PlayerAvatar.USERNAME_FIGURE);

            msg.writeInt(replyAuthor == null ? 0 : replyAuthor.getId()); // last message userid
            msg.writeString(replyAuthor == null ? "Unknown Player" : replyAuthor.getUsername()); // last message username
            msg.writeInt((int) Comet.getTime() - forumThread.getMostRecentPost().getAuthorTimestamp()); // last message time ago
            msg.writeByte(1); // state
            msg.writeInt(0); //admin id
            msg.writeString(""); // admin username
            msg.writeInt(0); // admin action time ago.
        }
    }
}