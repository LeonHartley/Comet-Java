package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.GroupMembersMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;


public class RevokeMembershipMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();
        int playerId = msg.readInt();

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null)
            return;

        if (playerId == group.getData().getOwnerId())
            return;

        GroupMember groupMember = group.getMembershipComponent().getMembers().get(client.getPlayer().getId());

        if (groupMember == null) {
            return;
        }

        if (!groupMember.getAccessLevel().isAdmin() && playerId != client.getPlayer().getId())
            return;

        group.getMembershipComponent().removeMembership(playerId);

        if (playerId == client.getPlayer().getId()) {
            if (client.getPlayer().getData().getFavouriteGroup() == groupId) {
                client.getPlayer().getData().setFavouriteGroup(0);
                client.getPlayer().getData().save();
            }

            if (client.getPlayer().getGroups().contains(groupId)) {
                client.getPlayer().getGroups().remove(client.getPlayer().getGroups().indexOf(groupId));
                client.send(group.composeInformation(true, client.getPlayer().getId()));
            }
        } else {
            if (PlayerManager.getInstance().isOnline(playerId)) {
                Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

                if (session != null) {
                    if (session.getPlayer().getData().getFavouriteGroup() == groupId) {
                        session.getPlayer().getData().setFavouriteGroup(0);
                        session.getPlayer().getData().save();
                    }


                    if (session.getPlayer().getGroups().contains(groupId)) {
                        session.getPlayer().getGroups().remove(session.getPlayer().getGroups().indexOf(groupId));
                    }
                }
            }

            client.send(new GroupMembersMessageComposer(group.getData(), 0, new ArrayList<>(group.getMembershipComponent().getMembersAsList()), 0, "", group.getMembershipComponent().getAdministrators().contains(client.getPlayer().getId())));
        }
    }
}
