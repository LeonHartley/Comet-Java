package com.cometproject.server.game.commands.staff;


import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.RoomEntityType;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class RoomAlertCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        for (GenericEntity entity : client.getPlayer().getEntity().getRoom().getEntities().getEntitiesCollection().values()) {
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                PlayerEntity playerEntity = (PlayerEntity) entity;

                playerEntity.getPlayer().getSession().send(AdvancedAlertMessageComposer.compose("Alert", this.merge(params)));
            }
        }
    }

    @Override
    public String getPermission() {
        return "roomalert_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roomalert.description");
    }
}
