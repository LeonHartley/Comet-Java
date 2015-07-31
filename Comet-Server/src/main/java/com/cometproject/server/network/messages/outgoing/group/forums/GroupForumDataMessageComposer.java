package com.cometproject.server.network.messages.outgoing.group.forums;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class GroupForumDataMessageComposer extends MessageComposer {
    private final Group group;
    private final int playerId;

    public  GroupForumDataMessageComposer(final Group group, int playerId) {
        this.group = group;
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.GroupForumDataMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        this.group.getForumComponent().composeData(msg);

        final ForumSettings forumSettings = this.group.getForumComponent().getForumSettings();

        msg.writeInt(forumSettings.getReadPermission().getPermissionId());
        msg.writeInt(forumSettings.getPostPermission().getPermissionId());
        msg.writeInt(forumSettings.getStartThreadsPermission().getPermissionId());
        msg.writeInt(forumSettings.getModeratePermission().getPermissionId());

        msg.writeString("");//1
        msg.writeString("");//2
        msg.writeString("");//3
        msg.writeString("");//4
        msg.writeString("");//??

        msg.writeBoolean(this.group.getData().getOwnerId() == this.playerId);
        msg.writeBoolean(true); // is staff?
    }
}
