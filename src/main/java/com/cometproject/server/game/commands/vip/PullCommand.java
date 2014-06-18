package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class PullCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        sendChat("Command disabled", client);
/*
        if (params.length == 0) {
            sendChat("Invalid username", client);
            return;
        }

        String username = params[0];
        Session user = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if (user == null) {
            return;
        }

        if (user.getPlayer().getEntity() == null) {
            return;
        }

        if (user == client) {
            sendChat(Locale.get("command.pull.playerhimself"), client);
            return;
        }

        Position3D playerPosition = client.getPlayer().getEntity().getPosition();
        Position3D pulledPlayerPosition = user.getPlayer().getEntity().getPosition();

        if(!DistanceCalculator.tilesTouching(playerPosition, pulledPlayerPosition)) {
            return;
        }

        RoomModel model = client.getPlayer().getEntity().getRoom().getModel();

       // if (model.getDoorX() == posX && model.getDoorY() == posY) {
       //    sendChat(Locale.get(""), client);
       //     return;
       // }

       // user.getPlayer().getEntity().setWalkingGoal(posX, posY);

        List<Square> path = user.getPlayer().getEntity().getPathfinder().makePath();
        user.getPlayer().getEntity().unIdle();

        if (user.getPlayer().getEntity().getWalkingPath() != null)
            user.getPlayer().getEntity().getWalkingPath().clear();

        user.getPlayer().getEntity().setWalkingPath(path);

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(
                TalkMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), Locale.get("command.pull.message").replace("%playername%", user.getPlayer().getData().getUsername()), 0, 0)
        );*/

    }


    @Override
    public String getPermission() {
        return "pull_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.pull.description");
    }
}
