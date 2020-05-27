package com.cometproject.api.game.groups.types.components.membership;

import com.cometproject.api.game.players.data.PlayerAvatar;

public interface IGroupMember {
    int getMembershipId();

    void setMembershipId(int membershipId);

    int getPlayerId();

    int getGroupId();

    GroupAccessLevel getAccessLevel();

    void setAccessLevel(GroupAccessLevel accessLevel);

    int getDateJoined();
}
