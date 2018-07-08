package com.cometproject.server.game.commands.vip;

import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.types.EntityPathfinder;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.List;


public class SuperPullCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length == 0) {
            sendNotif(Locale.getOrDefault("command.user.invalid", "Invalid username!"), client);
            return;
        }

        if (client.getPlayer().getEntity().isRoomMuted() || client.getPlayer().getEntity().getRoom().getRights().hasMute(client.getPlayer().getId())) {
            sendNotif(Locale.getOrDefault("command.user.muted", "You are muted."), client);
            return;
        }

        String username = params[0];
        Session pulledSession = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (pulledSession == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (pulledSession.getPlayer().getEntity() == null) {
            sendNotif(Locale.getOrDefault("command.user.notinroom", "This user is not in a room."), client);
            return;
        }

        if (username.equals(client.getPlayer().getData().getUsername())) {
            sendNotif(Locale.get("command.pull.playerhimself"), client);
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();
        PlayerEntity pulledEntity = pulledSession.getPlayer().getEntity();

        Position squareInFront = client.getPlayer().getEntity().getPosition().squareInFront(client.getPlayer().getEntity().getBodyRotation());

        pulledEntity.setWalkingGoal(squareInFront.getX(), squareInFront.getY());

        List<Square> path = EntityPathfinder.getInstance().makePath(pulledEntity, pulledEntity.getWalkingGoal());
        pulledEntity.unIdle();

        if (pulledEntity.getWalkingPath() != null)
            pulledEntity.getWalkingPath().clear();

        pulledEntity.setWalkingPath(path);

        room.getEntities().broadcastMessage(
                new TalkMessageComposer(client.getPlayer().getEntity().getId(), Locale.get("command.pull.message").replace("%playername%", pulledEntity.getUsername()), ChatEmotion.NONE, 0)
        );
    }

    @Override
    public String getPermission() {
        return "superpull_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.superpull.description");
    }

    @Override
    public boolean canDisable() {
        return true;
    }
}
