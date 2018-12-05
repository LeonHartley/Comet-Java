package com.cometproject.server.game.commands.user.room;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;


public class PickAllCommand extends ChatCommand {
    private String logDesc = "";

    @Override
    public void execute(Session client, String message[]) {
        Room room = client.getPlayer().getEntity().getRoom();

//        if (room == null || !room.getData().getOwner().equals(client.getPlayer().getData().getUsername())) {
//            sendNotif(Locale.getOrDefault("command.need.rights", "You have no rights to use this command in this room."), client);
//            return;
//        }

        List<RoomItem> itemsToRemove = new ArrayList<>();

        itemsToRemove.addAll(room.getItems().getFloorItems().values());
        itemsToRemove.addAll(room.getItems().getWallItems().values());

        for (RoomItem item : itemsToRemove) {
            if (item instanceof RoomItemFloor && item.getItemData().getOwnerId() == client.getPlayer().getId()) {
                room.getItems().removeItem((RoomItemFloor) item, client);
            } else if (item instanceof RoomItemWall && item.getItemData().getOwnerId() == client.getPlayer().getId()) {
                room.getItems().removeItem((RoomItemWall) item, client, true);
            }
        }

        itemsToRemove.clear();

        this.logDesc = "El staff %s ha hecho pickall en la sala '%b', cuyo dueño es %c"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", room.getData().getName())
                .replace("%c", room.getData().getOwner());
    }

    @Override
    public String getPermission() {
        return "pickall_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.pickall.description");
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
