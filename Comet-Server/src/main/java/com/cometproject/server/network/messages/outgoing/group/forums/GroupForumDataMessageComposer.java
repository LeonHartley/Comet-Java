package com.cometproject.server.network.messages.outgoing.group.forums;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GroupForumDataMessageComposer extends MessageComposer {
    private final Group group;
    private final int playerId;

    public GroupForumDataMessageComposer(final Group group, int playerId) {
        this.group = group;
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.GroupForumDataMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(group.getId());
        msg.writeString(group.getData().getTitle());
        msg.writeString(group.getData().getDescription());
        msg.writeString(group.getData().getBadge());
        msg.writeInt(0);//total threads
        msg.writeInt(0);//leaderboard score
        msg.writeInt(0);//total messages
        msg.writeInt(0);//unread messages
        msg.writeInt(0);//last message id
        msg.writeInt(0);//last message author id
        msg.writeString("");//last message author name
        msg.writeInt(0);//last message time
        msg.writeInt(1);// read permissions
        msg.writeInt(1);// write permissions
        msg.writeInt(1);// post thread permissions
        msg.writeInt(1);// can moderate
        msg.writeString("");//1
        msg.writeString("");//2
        msg.writeString("");//3
        msg.writeString("");//4
        msg.writeString("");//??
        msg.writeBoolean(group.getData().getOwnerId() == playerId);
        msg.writeBoolean(true);
    }
}
