package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.alerts.RoomAlertMessage;


public class RoomAlertCommand extends ChatCommand {
    private String logDesc;

    @Override
    public void execute(Session client, String[] params) {
        final WsMessage msg = new RoomAlertMessage(this.merge(params), client.getPlayer().getData().getUsername(), client.getPlayer().getData().getFigure());

        for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
            if (playerEntity.getPlayer().getSession().getWsChannel() != null) {
                playerEntity.getPlayer().getSession().sendWs(msg);
            } else {
                playerEntity.getPlayer().getSession().send(new AlertMessageComposer(this.merge(params)));
            }
        }

        this.logDesc = "-c executed roomalert in -d. [-e]"
                .replace("-c", client.getPlayer().getData().getUsername())
                .replace("-d", client.getPlayer().getEntity().getRoom().getData().getName())
                .replace("-e", this.merge(params));
    }

    @Override
    public String getPermission() {
        return "roomalert_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.message", "%message%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roomalert.description");
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
