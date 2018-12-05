package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class RoomKickCommand extends ChatCommand {
    private String logDesc;

    @Override
    public void execute(Session client, String[] params) {
        for (RoomEntity entity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                PlayerEntity playerEntity = (PlayerEntity) entity;

                if (playerEntity.getPlayer().getPermissions().getRank().roomKickable()) {
                    playerEntity.getPlayer().getSession().send(new AdvancedAlertMessageComposer(Locale.get("command.roomkick.title"), this.merge(params)));
                    playerEntity.kick();
                }
            }
        }

        this.logDesc = "El Staff -c ha dado RoomKick en la sala -d"
                .replace("-c", client.getPlayer().getData().getUsername())
                .replace("-d", client.getPlayer().getEntity().getRoom().getData().getName());
    }

    @Override
    public String getPermission() {
        return "roomkick_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.message", "%message%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roomkick.description");
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
