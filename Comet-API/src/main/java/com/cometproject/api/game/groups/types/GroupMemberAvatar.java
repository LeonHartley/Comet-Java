package com.cometproject.api.game.groups.types;

import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.game.players.data.PlayerAvatar;

public class GroupMemberAvatar {
    private final PlayerAvatar playerAvatar;
    private final boolean isRequest;
    private final IGroupMember groupMember;

    public GroupMemberAvatar(PlayerAvatar playerAvatar, boolean isRequest, IGroupMember groupMember) {
        this.playerAvatar = playerAvatar;
        this.isRequest = isRequest;
        this.groupMember = groupMember;
    }

    public PlayerAvatar getPlayerAvatar() {
        return playerAvatar;
    }

    public IGroupMember getGroupMember() {
        return groupMember;
    }

    public boolean isRequest() {
        return isRequest;
    }
}
