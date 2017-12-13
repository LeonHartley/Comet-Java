package com.cometproject.game.groups.factories;

import com.cometproject.api.game.groups.IGroupService;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.IMembershipComponent;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.game.groups.types.Group;
import com.cometproject.game.groups.types.components.MembershipComponent;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupFactory {
    private final IGroupService groupService;

    public GroupFactory(IGroupService groupService) {
        this.groupService = groupService;
    }

    public IGroup createGroupInstance(IGroupData groupData, Map<Integer, IGroupMember> groupMembers,
                                      Set<Integer> membershipRequests, Set<Integer> administrators) {
        final IMembershipComponent membershipComponent = new MembershipComponent(groupData.getId(), this.groupService,
                groupMembers, membershipRequests, administrators);

        final IGroup group = new Group(groupData, membershipComponent, null);

        // Do anything else to it?

        return group;
    }
}
