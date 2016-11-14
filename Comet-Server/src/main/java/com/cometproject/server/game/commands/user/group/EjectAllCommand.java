package com.cometproject.server.game.commands.user.group;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.List;

public class EjectAllCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final List<RoomItem> itemsToRemove = Lists.newArrayList();

        for(RoomItemFloor roomItemFloor : client.getPlayer().getEntity().getRoom().getItems().getFloorItems().values()) {
            if(roomItemFloor.getOwner() == client.getPlayer().getId()) {
                itemsToRemove.add(roomItemFloor);
            }
        }

        for(RoomItemWall roomItemWall : client.getPlayer().getEntity().getRoom().getItems().getWallItems().values()) {
            if(roomItemWall.getOwner() == client.getPlayer().getId()) {
                itemsToRemove.add(roomItemWall);
            }
        }

        for(RoomItem item : itemsToRemove) {
            if(item instanceof RoomItemFloor) {
                client.getPlayer().getEntity().getRoom().getItems().removeItem((RoomItemFloor) item, client);
            } else {
                client.getPlayer().getEntity().getRoom().getItems().removeItem(((RoomItemWall) item), client, true);
            }
        }
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
}
