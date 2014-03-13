package com.cometproject.server.game.commands.vip;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.avatars.pathfinding.Square;
import com.cometproject.server.game.rooms.types.RoomModel;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.LinkedList;
import java.util.List;

public class PushCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(params.length == 0) {
            this.sendChat("Invalid username", client);
            return;
        }

        String username = params[0];
        Session user = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if(!client.getPlayer().getData().isVip()) {
            this.sendChat("You must be VIP to use this command!", client);
            return;
        }

        if(user == null) {
            return;
        }

        if(user.getPlayer().getEntity() == null) {
            return;
        }

        if(user == client) {
            this.sendChat("You can't push yourself!", client);
            return;
        }

        int posX = user.getPlayer().getEntity().getPosition().getX();
        int posY = user.getPlayer().getEntity().getPosition().getY();
        int playerX = client.getPlayer().getEntity().getPosition().getX();
        int playerY = client.getPlayer().getEntity().getPosition().getY();
        int rot = client.getPlayer().getEntity().getBodyRotation();

        if (!((Math.abs((posX - playerX)) >= 2) || (Math.abs(posY - playerY) >= 2))) {
            switch(rot) {
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

            RoomModel model = client.getPlayer().getEntity().getRoom().getModel();

            if(model.getDoorX() == posX && model.getDoorY() == posY) {
                this.sendChat("You can't push a user out of the room!", client);
                return;
            }

            user.getPlayer().getEntity().setWalkingGoal(posX, posY);

            List<Square> path = user.getPlayer().getEntity().getPathfinder().makePath();
            user.getPlayer().getEntity().unIdle();

            if(user.getPlayer().getEntity().getWalkingPath() != null)
                user.getPlayer().getEntity().getWalkingPath().clear();

            user.getPlayer().getEntity().setWalkingPath(path);

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(
                    TalkMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), "*pushes " + user.getPlayer().getData().getUsername() + "*", 0, 0)
            );
        }
    }

    @Override
    public String getPermission() {
        return "push_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.push.description");
    }
}
