package com.cometproject.game.groups.types.components;

import com.cometproject.api.game.groups.IGroupService;
import com.cometproject.api.game.groups.types.components.IMembershipComponent;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.api.networking.sessions.ISessionService;

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
    public void dispose() {
        this.groupMembers.clear();
        this.membershipRequests.clear();
        this.administrators.clear();
    }

    @Override
    public void broadcastMessage(ISessionService sessionService, IMessageComposer messageComposer, int sender) {
        // Not implemented yet, might be revisited when module api for messenger is fleshed out, this won't be needed
    }

    @Override
    public boolean hasMembership(int playerId) {
        return this.groupMembers.containsKey(playerId);
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
