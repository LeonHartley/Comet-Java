package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class RoomNotificationCommand extends NotificationCommand {

    @Override
    protected void globalNotification(String image, String message, Session client) {
        for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
            playerEntity.getPlayer().getSession().send(new NotificationMessageComposer(image, message));
        }
    }

    @Override
    public String getPermission() {
        return "roomnotification_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.message", "%message%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roomnotification.description");
    }

}
