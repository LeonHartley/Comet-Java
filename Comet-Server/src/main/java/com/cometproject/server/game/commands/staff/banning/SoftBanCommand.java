package com.cometproject.server.game.commands.staff.banning;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;


public class SoftBanCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            sendNotif(Locale.getOrDefault("command.params.length", "Oops! You did something wrong!"), client);
            return;
        }

        String username = params[0];

        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (user == client || !user.getPlayer().getPermissions().getRank().bannable() || user.getPlayer().getPermissions().getRank().getId() >= client.getPlayer().getPermissions().getRank().getId()) {
            sendNotif(Locale.getOrDefault("command.user.notbannable", "You're not able to ban this user!"), client);
            return;
        }

        client.getPlayer().getStats().addBan();

        user.disconnect("banned");

        long expire = Comet.getTime() + (2 * 3600);

        BanManager.getInstance().banPlayer(BanType.USER, user.getPlayer().getId() + "", 2, expire, params.length > 1 ? this.merge(params, 1) : "", client.getPlayer().getId());

        this.logDesc = "El staff %s ha hecho softBan< en la sala '%b' al usuario %u"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", client.getPlayer().getEntity().getRoom().getData().getName())
                .replace("%u", username);
    }

    @Override
    public String getPermission() {
        return "softban_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.softban", "%username% %reason%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.softban.description");
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
    public boolean Loggable(){
        return true;
    }
}
