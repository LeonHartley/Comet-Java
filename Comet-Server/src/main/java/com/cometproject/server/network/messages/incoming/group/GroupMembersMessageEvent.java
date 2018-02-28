package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.server.composers.group.GroupMembersMessageComposer;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


public class GroupMembersMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int groupId = msg.readInt();
        final int page = msg.readInt();
        final String searchQuery = msg.readString();
        final int requestType = msg.readInt();

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null)
            return;

        final List<PlayerAvatar> playerAvatars = Lists.newArrayList();

        switch (requestType) {
            default: {
                for (IGroupMember groupMember : group.getMembers().getMembersAsList()) {
                    addPlayerAvatar(groupMember.getPlayerId(), playerAvatars, (playerAvatar -> {
                        playerAvatar.tempData(groupMember);
                    }));
                }
            }
            break;
            case 1: {
                for (Integer adminId : group.getMembers().getAdministrators()) {
                    addPlayerAvatar(adminId, playerAvatars);
                }

            }
            break;

            case 2: {
                for (Integer requestPlayerId : group.getMembers().getMembershipRequests()) {
                    addPlayerAvatar(requestPlayerId, playerAvatars);
                }
            }
            break;

        }

        final Set<PlayerAvatar> playersToRemove = Sets.newHashSet();

        if (!searchQuery.isEmpty()) {
            for (PlayerAvatar playerAvatar : playerAvatars) {
                if (!playerAvatar.getUsername().toLowerCase().startsWith(searchQuery.toLowerCase())) {
                    playersToRemove.add(playerAvatar);
                }
            }
        }

        playerAvatars.removeAll(playersToRemove);

        client.send(new GroupMembersMessageComposer(group.getData(), page, playerAvatars, requestType, searchQuery, group.getMembers().getAdministrators().contains(client.getPlayer().getId())));
    }

    private void addPlayerAvatar(final int playerId, final List<PlayerAvatar> playerAvatars, Consumer<PlayerAvatar> avatarConsumer) {

        final PlayerAvatar playerAvatar = PlayerManager.getInstance().getAvatarByPlayerId(playerId, PlayerAvatar.USERNAME_FIGURE);

        if (playerAvatar != null) {
            if (avatarConsumer != null) {
                avatarConsumer.accept(playerAvatar);
            }

            playerAvatars.add(playerAvatar);
        }

    }

    private void addPlayerAvatar(final int playerId, final List<PlayerAvatar> playerAvatars) {
        addPlayerAvatar(playerId, playerAvatars, null);
    }
}
