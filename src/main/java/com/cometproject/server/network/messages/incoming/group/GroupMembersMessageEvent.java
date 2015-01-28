package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.GroupMembersMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.ArrayList;
import java.util.List;


public class GroupMembersMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();
        int page = msg.readInt();
        String searchQuery = msg.readString();
        int requestType = msg.readInt();

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null)
            return;

        List<Object> groupMembers;

        switch (requestType) {
            default:
                groupMembers = new ArrayList<>(group.getMembershipComponent().getMembersAsList());
                break;
            case 1:
                groupMembers = new ArrayList<>(group.getMembershipComponent().getAdministrators());
                break;
            case 2:
                groupMembers = new ArrayList<>(group.getMembershipComponent().getMembershipRequests());
                break;
        }

        if (!searchQuery.isEmpty()) {
            List<Object> toRemove = new ArrayList<>();

            for (Object obj : groupMembers) {
                String username = PlayerDao.getUsernameByPlayerId(obj instanceof GroupMember ? ((GroupMember) obj).getPlayerId() : (int) obj);

                if (!username.toLowerCase().startsWith(searchQuery.toLowerCase()))
                    toRemove.add(obj);
            }

            for (Object obj : toRemove) {
                groupMembers.remove(obj);
            }

            toRemove.clear();
        }

        client.send(GroupMembersMessageComposer.compose(group.getData(), page, groupMembers, requestType, searchQuery, group.getMembershipComponent().getAdministrators().contains(client.getPlayer().getId())));
    }
}
