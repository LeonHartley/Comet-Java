package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.server.composers.group.GroupInformationMessageComposer;
import com.cometproject.server.composers.group.GroupMembersMessageComposer;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.UpdateFriendStateMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.YouAreControllerMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Lists;

import java.util.List;


public class RevokeMembershipMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int playerId = msg.readInt();

        IGroup group = GameContext.getCurrent().getGroupService().getGroup(groupId);

        if (group == null)
            return;

        if (playerId == group.getData().getOwnerId())
            return;

        IGroupMember groupMember = group.getMembers().getAll().get(playerId);

        if (groupMember == null) {
            return;
        }

        final IGroupMember revokerMembership = group.getMembers().getAll().get(client.getPlayer().getId());

        if (!revokerMembership.getAccessLevel().isAdmin() && playerId != client.getPlayer().getId())
            return;

        GameContext.getCurrent().getGroupService().removeGroupMember(group, groupMember);

        List<RoomItem> itemsToRemove = Lists.newArrayList();

        if (RoomManager.getInstance().isActive(group.getData().getRoomId())) {
            final Room room = RoomManager.getInstance().get(group.getData().getRoomId());


            for (RoomItemFloor floorItem : room.getItems().getFloorItems().values()) {
                if (floorItem.getItemData().getOwnerId() == playerId) {
                    itemsToRemove.add(floorItem);
                }
            }

            for (RoomItemWall wallItem : room.getItems().getWallItems().values()) {
                if (wallItem.getItemData().getOwnerId() == playerId) {
                    itemsToRemove.add(wallItem);
                }
            }
        }

        if (playerId == client.getPlayer().getId()) {
            if (client.getPlayer().getData().getFavouriteGroup() == groupId) {
                client.getPlayer().getData().setFavouriteGroup(0);
                client.getPlayer().getData().save();
            }

            if (client.getPlayer().getGroups().contains(groupId)) {
                client.getPlayer().getGroups().remove(new Integer(groupId));

                client.send(new GroupInformationMessageComposer(group, GameContext.getCurrent().getRoomService().getRoomData(group.getData().getRoomId()), true,
                        client.getPlayer().getId() == group.getData().getOwnerId(), group.getMembers().getAdministrators().contains(client.getPlayer().getId()),
                        group.getMembers().getAll().containsKey(client.getPlayer().getId()) ? 1 : group.getMembers().getMembershipRequests().contains(client.getPlayer().getId()) ? 2 : 0));

            }

            if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom().getId() == group.getData().getRoomId()) {
                client.send(new YouAreControllerMessageComposer(0));

                client.getPlayer().getEntity().removeStatus(RoomEntityStatus.CONTROLLER);
                client.getPlayer().getEntity().addStatus(RoomEntityStatus.CONTROLLER, "0");
                client.getPlayer().getEntity().markNeedsUpdate();
            }

            if (CometSettings.groupChatEnabled) {
                client.send(new UpdateFriendStateMessageComposer(-1, -groupId));
            }

            this.ejectItems(itemsToRemove, client);
        } else {
            if (PlayerManager.getInstance().isOnline(playerId)) {
                Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

                if (session != null) {
                    if (session.getPlayer().getData().getFavouriteGroup() == groupId) {
                        session.getPlayer().getData().setFavouriteGroup(0);
                        session.getPlayer().getData().save();
                    }

                    if (session.getPlayer().getEntity() != null && session.getPlayer().getEntity().getRoom().getId() == group.getData().getRoomId()) {
                        session.send(new YouAreControllerMessageComposer(0));

                        session.getPlayer().getEntity().removeStatus(RoomEntityStatus.CONTROLLER);
                        session.getPlayer().getEntity().addStatus(RoomEntityStatus.CONTROLLER, "0");
                        session.getPlayer().getEntity().markNeedsUpdate();
                    }

                    if (session.getPlayer().getGroups().contains(groupId)) {
                        session.getPlayer().getGroups().remove(groupId);

                        if (CometSettings.groupChatEnabled) {
                            session.send(new UpdateFriendStateMessageComposer(-1, -groupId));
                        }
                    }

                    this.ejectItems(itemsToRemove, session);
                } else {
                    this.ejectItems(itemsToRemove, null);
                }
            }

            client.send(new GroupMembersMessageComposer(group.getData(), 0,
                    group.getMembers().getMemberAvatars(), 0, "",
                    group.getMembers().getAdministrators().contains(client.getPlayer().getId())));
        }

        itemsToRemove.clear();
    }

    private void ejectItems(List<RoomItem> items, Session player) {
        for (RoomItem roomItem : items) {
            if (roomItem instanceof RoomItemFloor) {
                roomItem.getRoom().getItems().removeItem(((RoomItemFloor) roomItem), player);
            } else if (roomItem instanceof RoomItemWall) {
                roomItem.getRoom().getItems().removeItem(((RoomItemWall) roomItem), player, true);
            }
        }
    }
}
