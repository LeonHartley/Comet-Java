package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.game.players.data.components.messenger.RelationshipLevel;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class UpdateFriendStateMessageComposer extends MessageComposer {
    private final PlayerAvatar playerAvatar;
    private final IGroupData group;

    private final boolean online;
    private final boolean inRoom;
    private final RelationshipLevel relationshipLevel;
    private int action;
    private int friendId;

    public UpdateFriendStateMessageComposer(final PlayerAvatar playerAvatar, final boolean online, final boolean inRoom, final RelationshipLevel level) {
        this.playerAvatar = playerAvatar;
        this.group = null;
        this.online = online;
        this.inRoom = inRoom;
        this.relationshipLevel = level;
    }

    public UpdateFriendStateMessageComposer(final IGroupData group) {
        this.playerAvatar = null;
        this.group = group;
        this.online = true;
        this.inRoom = false;
        this.relationshipLevel = null;
    }

    public UpdateFriendStateMessageComposer(int action, int friendId) {
        this.playerAvatar = null;
        this.group = null;
        this.online = false;
        this.inRoom = false;
        this.action = action;
        this.friendId = friendId;
        this.relationshipLevel = null;
    }

    @Override
    public short getId() {
        return Composers.FriendListUpdateMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (this.playerAvatar == null && this.group == null) {
            msg.writeInt(0);
            msg.writeInt(1);
            msg.writeInt(this.action);
            msg.writeInt(this.friendId);

            return;
        }

        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(0);

        msg.writeInt(this.group != null ? -this.group.getId() : this.playerAvatar.getId());
        msg.writeString(this.group != null ? this.group.getTitle() : this.playerAvatar.getUsername());
        msg.writeInt(77);
        msg.writeBoolean(online);
        msg.writeBoolean(inRoom);
        msg.writeString(this.group != null ? this.group.getBadge() : this.playerAvatar.getFigure());
        msg.writeInt(this.group != null ? 1 : 0);
        msg.writeString(this.group != null ? "" : this.playerAvatar.getMotto());
        msg.writeString(""); // facebook name ?
        msg.writeString("");
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeShort(this.relationshipLevel != null ? relationshipLevel.getLevelId() : 0);
    }
}
