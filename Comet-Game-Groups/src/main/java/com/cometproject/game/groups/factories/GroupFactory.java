package com.cometproject.game.groups.factories;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupFactory {
    public GroupFactory() {

    }

    public IGroup createGroupInstance(IGroupData groupData, Map<Integer, IGroupMember> groupMembers,
                                      Set<Integer> membershipRequests, Set<Integer> administrators) {
        return null;
    }
}
