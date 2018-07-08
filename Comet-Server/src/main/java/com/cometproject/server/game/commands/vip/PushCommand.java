package com.cometproject.server.game.commands.vip;

import com.cometproject.api.game.rooms.models.IRoomModel;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.models.RoomModel;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.types.EntityPathfinder;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.List;


public class PushCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length == 0) {
            Position userPos = client.getPlayer().getEntity().getPosition().squareInFront(client.getPlayer().getEntity().getBodyRotation());
            Room room = client.getPlayer().getEntity().getRoom();
            List<RoomEntity> roomEntities = room.getEntities().getEntitiesAt(userPos);
            for (RoomEntity roomEntity : roomEntities) {
                push(roomEntity, client);
            }
        }

        if (client.getPlayer().getEntity().isRoomMuted() || client.getPlayer().getEntity().getRoom().getRights().hasMute(client.getPlayer().getId())) {
            sendNotif(Locale.getOrDefault("command.user.muted", "You are muted."), client);
            return;
        }

        String username = params[0];
        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (user.getPlayer().getEntity() == null) {
            sendNotif(Locale.getOrDefault("command.user.notinroom", "This user is not in a room."), client);
            return;
        }

        if (user == client) {
            sendNotif(Locale.get("command.push.playerhimself"), client);
            return;
        }

        if (user.getPlayer().getEntity().isOverriden()) {
            return;
        }

        push(user.getPlayer().getEntity(), client);
    }

    private void push(RoomEntity entity, Session client) {
        int posX = entity.getPosition().getX();
        int posY = entity.getPosition().getY();
        int playerX = client.getPlayer().getEntity().getPosition().getX();
        int playerY = client.getPlayer().getEntity().getPosition().getY();
        int rot = client.getPlayer().getEntity().getBodyRotation();

        if (!((Math.abs((posX - playerX)) >= 2) || (Math.abs(posY - playerY) >= 2))) {
            switch (rot) {
                case 4:
                    posY += 1;
                    break;

                case 0:
                    posY -= 1;
                    break;

                case 6:
                    posX -= 1;
                    break;

                case 2:
                    posX += 1;
                    break;

                case 3:
                    posX += 1;
                    posY += 1;
                    break;

                case 1:
                    posX += 1;
                    posY -= 1;
                    break;

                case 7:
                    posX -= 1;
                    posY -= 1;
                    break;

                case 5:
                    posX -= 1;
                    posY += 1;
                    break;
            }

            IRoomModel model = client.getPlayer().getEntity().getRoom().getModel();

            if (model.getDoorX() == posX && model.getDoorY() == posY) {
                return;
            }

            entity.setWalkingGoal(posX, posY);

            List<Square> path = EntityPathfinder.getInstance().makePath(entity, entity.getWalkingGoal());
            entity.unIdle();

            if (entity.getWalkingPath() != null)
                entity.getWalkingPath().clear();

            entity.setWalkingPath(path);

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(
                    new TalkMessageComposer(client.getPlayer().getEntity().getId(), Locale.get("command.push.message").replace("%playername%", entity.getUsername()), ChatEmotion.NONE, 0)
            );
        } else {
            client.getPlayer().getSession().send(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), Locale.getOrDefault("command.notaround", "Oops! %playername% is not near, walk to this player.").replace("%playername%", entity.getUsername()), 34));
        }
    }

    @Override
    public String getPermission() {
        return "push_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.push.description");
    }

    @Override
    public boolean canDisable() {
        return true;
    }
}
