package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.network.sessions.Session;

public class MassTeleportCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        for(RoomEntity roomEntity : client.getPlayer().getEntity().getRoom().getEntities().getAllEntities().values()) {
            roomEntity.teleportToEntity(client.getPlayer().getEntity());
        }
    }

    @Override
    public String getPermission() {
        return "massteleport_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.massteleport.description");
    }
}
