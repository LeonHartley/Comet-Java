package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class RoomKickCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        for (GenericEntity entity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                PlayerEntity playerEntity = (PlayerEntity) entity;

                if (playerEntity.getPlayer().getPermissions().getRank().roomKickable()) {
                    playerEntity.getPlayer().getSession().send(new AdvancedAlertMessageComposer(Locale.get("command.roomkick.title"), this.merge(params)));
                    playerEntity.kick();
                }
            }
        }
    }

    @Override
    public String getPermission() {
        return "roomkick_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roomkick.description");
    }
}
