package com.cometproject.server.game.commands.vip;

import com.cometproject.api.game.GameContext;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SendFloorItemMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class HideWiredCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final Room room = client.getPlayer().getEntity().getRoom();

        if (!client.getPlayer().getPermissions().getRank().roomFullControl() && !client.getPlayer().getEntity().hasRights()) {
            return;
        }

        String msg = "";

        if (client.getPlayer().getEntity().getRoom().getData().isWiredHidden()) {
            // show wireds
            room.getData().setIsWiredHidden(false);
            msg = Locale.getOrDefault("command.hidewired.shown", "Wired is now visible");

            for (RoomItemFloor floorItem : room.getItems().getFloorItems().values()) {
                if (floorItem instanceof WiredFloorItem) {
                    room.getEntities().broadcastMessage(new SendFloorItemMessageComposer(floorItem));
                }
            }

        } else {
            // hide wireds
            room.getData().setIsWiredHidden(true);
            msg = Locale.getOrDefault("command.hidewired.hidden", "Wired is now hidden");

            for (RoomItemFloor floorItem : room.getItems().getFloorItems().values()) {

                if (floorItem instanceof WiredFloorItem) {
                    room.getEntities().broadcastMessage(new RemoveFloorItemMessageComposer(floorItem.getVirtualId(),
                            client.getPlayer().getId()));
                }
            }
        }

        sendNotif(msg, client);

        GameContext.getCurrent().getRoomService().saveRoomData(room.getData());
    }

    @Override
    public String getPermission() {
        return "hidewired_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.hidewired.description");
    }
}
