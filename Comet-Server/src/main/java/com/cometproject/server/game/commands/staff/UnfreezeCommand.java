package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;


public class UnfreezeCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {

        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.unfreeze.none", "Who do you want to unfreeze?"), client);
            return;
        }

        String username = params[0];
        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (username.equals(client.getPlayer().getData().getUsername())) {
            return;
        }

        if (user.getPlayer().getEntity() == null) {
            sendNotif(Locale.getOrDefault("command.user.notinroom", "This user is not in a room."), client);
            return;
        }

        user.getPlayer().getEntity().setCanWalk(true);
        isExecuted(client);

        this.logDesc = "El staff %s ha hecho unfreeze en la sala '%b'"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", client.getPlayer().getEntity().getRoom().getData().getName());
    }

    @Override
    public String getPermission() {
        return "unfreeze_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.unfreeze.description");
    }

    @Override
    public boolean bypassFilter() {
        return true;
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
