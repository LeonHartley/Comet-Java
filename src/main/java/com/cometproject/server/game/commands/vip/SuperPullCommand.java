package com.cometproject.server.game.commands.vip;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Pathfinder;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.List;

public class SuperPullCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length == 0) {
            sendChat("Invalid username", client);
            return;
        }

        String username = params[0];
        Session pulledSession = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if (pulledSession == null) {
            return;
        }

        if (pulledSession.getPlayer().getEntity() == null) {
            return;
        }

        if (username.equals(client.getPlayer().getData().getUsername())) {
            sendChat(Locale.get("command.pull.playerhimself"), client);
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();
        PlayerEntity pulledEntity = pulledSession.getPlayer().getEntity();
        
        Position squareInFront = client.getPlayer().getEntity().getPosition().squareInFront(client.getPlayer().getEntity().getBodyRotation());

        pulledEntity.setWalkingGoal(squareInFront.getX(), squareInFront.getY());

        List<Square> path = Pathfinder.getInstance().makePath(pulledEntity, pulledEntity.getWalkingGoal());
        pulledEntity.unIdle();

        if (pulledEntity.getWalkingPath() != null)
            pulledEntity.getWalkingPath().clear();

        pulledEntity.setWalkingPath(path);

        room.getEntities().broadcastMessage(
                TalkMessageComposer.compose(client.getPlayer().getEntity().getId(), Locale.get("command.pull.message").replace("%playername%", pulledEntity.getUsername()), 0, 0)
        );
    }

    @Override
    public String getPermission() {
        return "superpull_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.superpull.description");
    }
}
