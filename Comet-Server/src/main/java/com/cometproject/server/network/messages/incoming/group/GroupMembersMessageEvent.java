package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.GroupMemberAvatar;
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


        List<GroupMemberAvatar> avatars;
        switch (requestType) {
            default: {
                avatars = group.getMembers().getMemberAvatars();
            }
            break;
            case 1: {
                avatars = group.getMembers().getAdminAvatars();
            }
            break;

            case 2: {
                avatars = group.getMembers().getRequestAvatars();
            }
            break;

        }

        final Set<GroupMemberAvatar> playersToRemove = Sets.newHashSet();

        if (!searchQuery.isEmpty()) {
            for (GroupMemberAvatar playerAvatar : avatars) {
                if (!playerAvatar.getPlayerAvatar().getUsername().toLowerCase().startsWith(searchQuery.toLowerCase())) {
                    playersToRemove.add(playerAvatar);
                }
            }
        }

        avatars.removeAll(playersToRemove);

        client.send(new GroupMembersMessageComposer(group.getData(), page, avatars, requestType, searchQuery, group.getMembers().getAdministrators().contains(client.getPlayer().getId())));
    }
}
