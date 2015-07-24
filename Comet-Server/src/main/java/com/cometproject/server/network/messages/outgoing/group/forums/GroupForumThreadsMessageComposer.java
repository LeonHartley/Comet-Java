package com.cometproject.server.network.messages.outgoing.group.forums;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThreadReply;
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

        for(ForumThread forumThread : this.group.getForumComponent().getForumThreads().values()) {
            msg.writeInt(forumThread.getId());

            final PlayerAvatar authorAvatar = PlayerManager.getInstance().getAvatarByPlayerId(forumThread.getAuthorId(), PlayerAvatar.USERNAME_FIGURE);

            msg.writeInt(authorAvatar.getId());
            msg.writeString(authorAvatar.getUsername());
            msg.writeString(forumThread.getTitle());
            msg.writeBoolean(false); // stickied.
            msg.writeBoolean(forumThread.isLocked());
            msg.writeInt(0); // create time
            msg.writeInt(0); // messages
            msg.writeInt(0); // unread messages
            msg.writeInt(2); // last message id
            msg.writeInt(authorAvatar.getId()); // last message userid
            msg.writeString(authorAvatar.getUsername()); // last message username
            msg.writeInt(forumThread.getAuthorTimestamp()); // last message time ago
            msg.writeByte(0); // state
            msg.writeInt(0); //admin id
            msg.writeString(""); // admin username
            msg.writeInt(0); // admin action time ago.
        }
    }
}
/*            k.threadId = _arg1._-4xh();
            k._-3Fy = _arg1._-4xh(); //  authorid
            k._-0Gz = _arg1.readString(); // author username
            k.header = _arg1.readString();
            k._-4ML = _arg1.readBoolean();//sticked
            k._-4OL = _arg1.readBoolean();//locked
            k._-53n = _arg1._-4xh(); // created time
            k._-2dx = _arg1._-4xh(); //messages
            k._-30E = _arg1._-4xh(); // unread messages
            k._-pr = _arg1._-4xh(); // last message id
            k._-1RW = _arg1._-4xh(); // last message userid
            k._-4nt = _arg1.readString(); // last message username
            k._-4bZ = _arg1._-4xh(); // last message time ago
            k.state = _arg1.readByte();
            k._-5Hb = _arg1._-4xh(); // admin id
            k._-OZ = _arg1.readString(); // admin username
            k._-211 = _arg1._-4xh(); // admin time ago.
            return (k);*/
