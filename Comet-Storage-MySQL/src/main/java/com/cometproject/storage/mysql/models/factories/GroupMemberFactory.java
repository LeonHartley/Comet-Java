package com.cometproject.storage.mysql.models.factories;

import com.cometproject.api.game.groups.types.components.membership.GroupAccessLevel;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.storage.mysql.models.GroupMemberData;

public class GroupMemberFactory {
    public IGroupMember create(final int membershipId, final int playerId, final int groupId, final GroupAccessLevel accessLevel, final int dateJoined) {
        return new GroupMemberData(membershipId, playerId, groupId, dateJoined, accessLevel);
    }

    public IGroupMember create(int playerId, int groupId, GroupAccessLevel accessLevel) {
        return new GroupMemberData(0, playerId, groupId, (int) (System.currentTimeMillis() / 1000), accessLevel);
    }
}
