package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;


public class MassHandItemCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.masshanditem.none", "To give everyone in the room an handitem type :masshanditem %number%"), client);
            return;
        }

        try {
            int handItem = Integer.parseInt(params[0]);

            for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                playerEntity.carryItem(handItem, false);
            }

        } catch (Exception e) {
            sendNotif(Locale.get("command.masshanditem.invalidid"), client);
        }

        this.logDesc = "El staff %s ha hecho <b>massHandItem</b> en la sala '%b'"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", client.getPlayer().getEntity().getRoom().getData().getName());
    }

    @Override
    public String getPermission() {
        return "masshanditem_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.number", "%number%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.masshanditem.description");
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