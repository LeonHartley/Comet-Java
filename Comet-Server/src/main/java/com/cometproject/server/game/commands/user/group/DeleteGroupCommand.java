package com.cometproject.server.game.commands.user.group;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.storage.api.StorageContext;
import com.google.common.collect.Lists;

import java.util.List;

public class DeleteGroupCommand extends ChatCommand {
    private String logDesc;
    private String groupName;

    @Override
    public void execute(Session client, String[] params) {
        if (client.getPlayer().getId() != client.getPlayer().getEntity().getRoom().getData().getOwnerId() && !client.getPlayer().getPermissions().getRank().roomFullControl()) {
            client.send(new AlertMessageComposer(Locale.getOrDefault("command.deletegroup.permission", "You don't have permission to delete this group!")));
            return;
        }

        if (!client.getPlayer().isDeletingGroup() || (Comet.getTime() - client.getPlayer().getDeletingGroupAttempt()) >= 30) {
            client.send(new AlertMessageComposer(Locale.getOrDefault("command.deletegroup.confirm", "Are you sure you want to delete this group? All items in the room will be returned to the rightful owner and the group will be deleted forever.<br><br>Use the command :deletegroup again to confirm!")));

            client.getPlayer().setDeletingGroup(true);
            client.getPlayer().setDeletingGroupAttempt(Comet.getTime());
            return;
        }

        final Room room = client.getPlayer().getEntity().getRoom();

        if (room.getGroup() != null) {
            IGroup group = room.getGroup();
            this.groupName = group.getData().getTitle();

            for (Integer groupMemberId : group.getMembers().getAll().keySet()) {
                Session groupMemberSession = NetworkManager.getInstance().getSessions().getByPlayerId(groupMemberId);

                List<RoomItem> floorItemsOwnedByPlayer = Lists.newArrayList();

                if (groupMemberId != group.getData().getOwnerId()) {
                    for (RoomItemFloor floorItem : room.getItems().getFloorItems().values()) {
                        if (floorItem.getItemData().getOwnerId() == groupMemberId) {
                            floorItemsOwnedByPlayer.add(floorItem);
                        }
                    }

                    for (RoomItemWall wallItem : room.getItems().getWallItems().values()) {
                        if (wallItem.getItemData().getOwnerId() == groupMemberId) {
                            floorItemsOwnedByPlayer.add(wallItem);
                        }
                    }
                }

                if (groupMemberSession != null && groupMemberSession.getPlayer() != null) {
                    groupMemberSession.getPlayer().getGroups().remove(new Integer(group.getId()));

                    if (groupMemberSession.getPlayer().getData().getFavouriteGroup() == group.getId()) {
                        groupMemberSession.getPlayer().getData().setFavouriteGroup(0);
                    }

                    for (RoomItem roomItem : floorItemsOwnedByPlayer) {
                        if (roomItem instanceof RoomItemFloor)
                            room.getItems().removeItem(((RoomItemFloor) roomItem), groupMemberSession);
                        else if (roomItem instanceof RoomItemWall)
                            room.getItems().removeItem(((RoomItemWall) roomItem), groupMemberSession, true);
                    }
                } else {
                    for (RoomItem roomItem : floorItemsOwnedByPlayer) {
                        StorageContext.getCurrentContext().getRoomItemRepository().removeItemFromRoom(roomItem.getId(), groupMemberId, roomItem.getItemData().getData());
                    }
                }

                floorItemsOwnedByPlayer.clear();
            }

            client.send(new AlertMessageComposer(Locale.getOrDefault("command.deletegroup.done", "The group was deleted successfully.")));
            GameContext.getCurrent().getGroupService().removeGroup(group.getId());

            room.setGroup(null);

            room.getData().setGroupId(0);
            GameContext.getCurrent().getRoomService().saveRoomData(room.getData());

            room.setIdleNow();
        } else {
            client.send(new AlertMessageComposer(Locale.getOrDefault("command.deletegroup.nogroup", "This room doesn't have a group to delete!")));
        }

        this.logDesc = "El Staff %s ha eliminado el grupo %b cuya sala es %d"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", this.groupName)
                .replace("%d", room.getData().getName());
    }

    @Override
    public String getPermission() {
        return "deletegroup_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.deletegroup.description");
    }

    @Override
    public String getLoggableDescription() {
        return this.logDesc;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }
}
