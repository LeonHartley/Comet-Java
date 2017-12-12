package com.cometproject.game.groups.types.components;

import com.cometproject.api.game.groups.IGroupService;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.IMembershipComponent;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.networking.messages.IMessageComposer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MembershipComponent implements IMembershipComponent {

    private final int groupId;

    private final IGroupService groupService;

    private final Map<Integer, IGroupMember> groupMembers;
    private final Set<Integer> membershipRequests;
    private final Set<Integer> administrators;

    public MembershipComponent(final int groupId, IGroupService groupService, Map<Integer, IGroupMember> groupMembers,
                               Set<Integer> membershipRequests, Set<Integer> administrators) {
        this.groupId = groupId;
        this.groupService = groupService;

        this.groupMembers = groupMembers;
        this.membershipRequests = membershipRequests;
        this.administrators = administrators;
    }


    @Override
    public IGroup getGroup() {
        return null;
    }

    @Override
    public void dispose() {
        this.groupMembers.clear();
        this.membershipRequests.clear();
        this.administrators.clear();
    }

    @Override
    public void broadcastMessage(IMessageComposer messageComposer, int sender) {

    }

    @Override
    public Map<Integer, IGroupMember> getAll() {
        return this.groupMembers;
    }

    @Override
    public List<IGroupMember> getMembersAsList() {
        return new ArrayList<>(this.groupMembers.values());
    }

    @Override
    public Set<Integer> getAdministrators() {
        return this.administrators;
    }

    @Override
    public Set<Integer> getMembershipRequests() {
        return this.membershipRequests;
    }
}
