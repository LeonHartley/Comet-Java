package com.cometproject.game.groups.types.components;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.IGroupService;
import com.cometproject.api.game.groups.types.components.IMembershipComponent;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.api.networking.sessions.ISessionService;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        // Not implemented yet, might be revisited when module api for messenger is fleshed out, this won't be needed
    }

    public void getAvatars(int type) {

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

    private List<PlayerAvatar> getMembers(int type) {
        final List<PlayerAvatar> playerAvatars = Lists.newArrayList();

        switch (type) {
            default: {
                for(IGroupMember groupMember : this.getMembersAsList()) {
                    addPlayerAvatar(groupMember.getPlayerId(), playerAvatars, (playerAvatar -> {
                        playerAvatar.tempData(groupMember);
                    }));
                }
            } break;
            case 1: {

                for(Integer adminId : this.getAdministrators()) {
                    addPlayerAvatar(adminId, playerAvatars);
                }
            } break;
            case 2: {
                for(Integer requestPlayerId : this.getMembershipRequests()) {
                    addPlayerAvatar(requestPlayerId, playerAvatars);
                }
            } break;

        }

        return playerAvatars;
    }

    @Override
    public List<PlayerAvatar> getMemberAvatars() {
        return this.getMembers(0);
    }

    @Override
    public List<PlayerAvatar> getAdminAvatars() {
        return this.getMembers(1);
    }

    @Override
    public List<PlayerAvatar> getRequestAvatars() {
        return this.getMembers(2);
    }

    private void addPlayerAvatar(final int playerId, final List<PlayerAvatar> playerAvatars, Consumer<PlayerAvatar> avatarConsumer) {
        final PlayerAvatar playerAvatar = GameContext.getCurrent().getPlayerService().getAvatarByPlayerId(playerId, PlayerAvatar.USERNAME_FIGURE);

        if(playerAvatar != null) {
            if(avatarConsumer != null) {
                avatarConsumer.accept(playerAvatar);
            }

            playerAvatars.add(playerAvatar);
        }

    }

    private void addPlayerAvatar(final int playerId, final List<PlayerAvatar> playerAvatars) {
        addPlayerAvatar(playerId, playerAvatars, null);
    }
}
