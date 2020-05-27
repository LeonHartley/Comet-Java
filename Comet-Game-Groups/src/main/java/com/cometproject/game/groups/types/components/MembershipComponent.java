package com.cometproject.game.groups.types.components;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.IGroupService;
import com.cometproject.api.game.groups.types.GroupMemberAvatar;
import com.cometproject.api.game.groups.types.components.IMembershipComponent;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.api.networking.sessions.ISessionService;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.Consumer;

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
        sessionService.broadcastTo(this.groupMembers.keySet(), messageComposer, sender);
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

    private List<GroupMemberAvatar> getMembers(int type) {
        final List<GroupMemberAvatar> playerAvatars = Lists.newArrayList();

        switch (type) {
            default: {
                for(IGroupMember groupMember : this.getMembersAsList()) {
                    addPlayerAvatar(groupMember.getPlayerId(), playerAvatars, false, groupMember);
                }
            } break;
            case 1: {
                for(Integer adminId : this.getAdministrators()) {
                    final IGroupMember member = this.getAll().get(adminId);
                    if (member != null) {
                        addPlayerAvatar(adminId, playerAvatars, false, member);
                    }
                }
            } break;
            case 2: {
                for(Integer request : this.getMembershipRequests()) {
                    addPlayerAvatar(request, playerAvatars, true, null);
                }
            } break;

        }

        return playerAvatars;
    }

    @Override
    public List<GroupMemberAvatar> getMemberAvatars() {
        return this.getMembers(0);
    }

    @Override
    public List<GroupMemberAvatar> getAdminAvatars() {
        return this.getMembers(1);
    }

    @Override
    public List<GroupMemberAvatar> getRequestAvatars() {
        final List<GroupMemberAvatar> avatars = Lists.newArrayList();

        for(Integer requestPlayerId : this.getMembershipRequests()) {
            final PlayerAvatar playerAvatar = GameContext.getCurrent().getPlayerService().getAvatarByPlayerId(requestPlayerId, PlayerAvatar.USERNAME_FIGURE);
            if (playerAvatar != null) {
                avatars.add(new GroupMemberAvatar(playerAvatar, true, null));
            }
        }

        return avatars;
    }

    private void addPlayerAvatar(final int playerId, final List<GroupMemberAvatar> playerAvatars, boolean isRequest, IGroupMember member) {
        final PlayerAvatar playerAvatar = GameContext.getCurrent().getPlayerService().getAvatarByPlayerId(playerId, PlayerAvatar.USERNAME_FIGURE);
        if (playerAvatar != null) {
            playerAvatars.add(new GroupMemberAvatar(playerAvatar, isRequest, member));
        }
    }
}
