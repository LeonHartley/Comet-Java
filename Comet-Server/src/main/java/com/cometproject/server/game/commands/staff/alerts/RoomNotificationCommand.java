package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class RoomNotificationCommand extends NotificationCommand {

    private String logDesc = "";

    @Override
    protected void globalNotification(String image, String message, Session client) {
        for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
            playerEntity.getPlayer().getSession().send(new NotificationMessageComposer(image, message));
        }

        this.logDesc = "El staff %s ha hecho roomnotification en la sala '%b' con parámetro %p"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", client.getPlayer().getEntity().getRoom().getData().getName())
                .replace("%p", message);
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

    @Override
    public String getLoggableDescription(){
        return this.logDesc;
    }

    @Override
    public boolean isLoggable(){
        return true;
    }

}
