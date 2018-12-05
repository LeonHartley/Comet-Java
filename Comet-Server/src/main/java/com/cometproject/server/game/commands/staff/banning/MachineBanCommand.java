package com.cometproject.server.game.commands.staff.banning;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;


public class MachineBanCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2) {
            sendNotif(Locale.getOrDefault("command.params.length", "Oops! You did something wrong!"), client);
            return;
        }

        String username = params[0];
        int length = Integer.parseInt(params[1]);

        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (user == client || !user.getPlayer().getPermissions().getRank().bannable() || user.getPlayer().getPermissions().getRank().getId() >= client.getPlayer().getPermissions().getRank().getId()) {
            sendNotif(Locale.getOrDefault("command.user.notbannable", "You're not able to ban this user!"), client);
            return;
        }

        long expire = Comet.getTime() + (length * 3600);

        String uniqueId = user.getUniqueId();

        if (BanManager.getInstance().hasBan(uniqueId, BanType.MACHINE)) {
            sendNotif("Machine ID: " + uniqueId + " is already banned.", client);
            return;
        }

        BanManager.getInstance().banPlayer(BanType.MACHINE, user.getUniqueId(), length, expire, params.length > 2 ? this.merge(params, 2) : "", client.getPlayer().getId());
        sendNotif("User has been machine ID banned (" + uniqueId + ")", client);

        user.disconnect("banned");

        this.logDesc = "El staff %s ha hecho machineban a '%u'"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%u", username);
    }

    @Override
    public String getPermission() {
        return "machineban_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.ban", "%username% %time% %reason%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.machineban.description");
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
