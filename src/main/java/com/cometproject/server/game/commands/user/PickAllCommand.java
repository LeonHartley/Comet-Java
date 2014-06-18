package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;

public class PickAllCommand extends ChatCommand {
    @Override
    public void execute(Session client, String message[]) {
        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || !room.getData().getOwner().equals(client.getPlayer().getData().getUsername())) {
            return;
        }

        List<RoomItem> itemsToRemove = new ArrayList<>();
        itemsToRemove.addAll(room.getItems().getFloorItems());
        itemsToRemove.addAll(room.getItems().getWallItems());

        for (RoomItem item : itemsToRemove) {
            if (item instanceof RoomItemFloor) {
                room.getItems().removeItem((RoomItemFloor) item, client);
            } else if (item instanceof RoomItemWall) {
                room.getItems().removeItem((RoomItemWall) item, client);
            }
        }
    }

    @Override
    public String getPermission() {
        return "pickall_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.pickall.description");
    }
}
