package com.cometproject.server.game.commands.user.group;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.storage.api.StorageContext;
import com.google.common.collect.Lists;

import java.util.List;

public class EjectAllCommand extends ChatCommand {
    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        Room room = client.getPlayer().getEntity().getRoom();
        IGroup group = room.getGroup();

        if (room.getData().getOwnerId() != client.getPlayer().getId()) {
            final List<RoomItem> itemsToRemove = Lists.newArrayList();

            for (RoomItemFloor roomItemFloor : client.getPlayer().getEntity().getRoom().getItems().getFloorItems().values()) {
                if (roomItemFloor.getItemData().getOwnerId() == client.getPlayer().getId()) {
                    itemsToRemove.add(roomItemFloor);
                }
            }

            for (RoomItemWall roomItemWall : client.getPlayer().getEntity().getRoom().getItems().getWallItems().values()) {
                if (roomItemWall.getItemData().getOwnerId() == client.getPlayer().getId()) {
                    itemsToRemove.add(roomItemWall);
                }
            }

            for (RoomItem item : itemsToRemove) {
                if (item instanceof RoomItemFloor) {
                    client.getPlayer().getEntity().getRoom().getItems().removeItem((RoomItemFloor) item, client);
                } else {
                    client.getPlayer().getEntity().getRoom().getItems().removeItem(((RoomItemWall) item), client, true);
                }
            }
        } else {
            for (Integer playerWithItem : room.getItems().getItemOwners().keySet()) {
                Session groupMemberSession = NetworkManager.getInstance().getSessions().getByPlayerId(playerWithItem);

                List<RoomItem> floorItemsOwnedByPlayer = Lists.newArrayList();

                for (RoomItemFloor floorItem : room.getItems().getFloorItems().values()) {

                    if (floorItem.getItemData().getOwnerId() == playerWithItem) {
                        floorItemsOwnedByPlayer.add(floorItem);
                    }
                }

                for (RoomItemWall wallItem : room.getItems().getWallItems().values()) {
                    if (wallItem.getItemData().getOwnerId() == playerWithItem) {
                        floorItemsOwnedByPlayer.add(wallItem);
                    }
                }

                if (groupMemberSession != null && groupMemberSession.getPlayer() != null && group != null) {
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
                    for (RoomItem item : floorItemsOwnedByPlayer) {
                        if (item instanceof RoomItemFloor) {
                            client.getPlayer().getEntity().getRoom().getItems().removeItem((RoomItemFloor) item, client, false);
                        } else {
                            client.getPlayer().getEntity().getRoom().getItems().removeItem(((RoomItemWall) item), client, false);
                        }
                    }
                }

                floorItemsOwnedByPlayer.clear();
            }
        }

        this.logDesc = "El usuario %s ha hecho ejectall en la sala '%b', cuyo dueño es %c"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", room.getData().getName())
                .replace("%c", room.getData().getOwner());
    }

    @Override
    public String getPermission() {
        return "ejectall_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.ejectall.description", "Removes all items you own in the room");
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