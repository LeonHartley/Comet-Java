package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class FollowCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            return;
        }

        Session leader = NetworkManager.getInstance().getSessions().getByPlayerUsername(params[0]);

        if (leader != null && leader.getPlayer() != null && leader.getPlayer().getEntity() != null) {
            // TODO: Does leader allow follow??

            if (leader.getPlayer().getPermissions().getRank().modTool()) {
                if (!client.getPlayer().getPermissions().getRank().modTool()) {
                    return;
                }
            }

            client.send(new RoomForwardMessageComposer(leader.getPlayer().getEntity().getRoom().getId()));
        } else {
            if (leader == null || leader.getPlayer() == null)
                sendNotif(Locale.get("command.follow.online"), client);
            else
                sendNotif(Locale.get("command.follow.room"), client);
        }
    }

    @Override
    public String getPermission() {
        return "follow_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.follow.description");
    }
}
