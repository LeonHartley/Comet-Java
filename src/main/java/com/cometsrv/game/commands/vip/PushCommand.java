package com.cometsrv.game.commands.vip;

import com.cometsrv.boot.Comet;
import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.game.rooms.avatars.pathfinding.Square;
import com.cometsrv.game.rooms.types.RoomModel;
import com.cometsrv.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometsrv.network.sessions.Session;

import java.util.LinkedList;

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

        if(user.getPlayer().getAvatar() == null) {
            return;
        }

        if(user == client) {
            this.sendChat("You can't push yourself!", client);
            return;
        }

        int posX = user.getPlayer().getAvatar().getPosition().getX();
        int posY = user.getPlayer().getAvatar().getPosition().getY();
        int playerX = client.getPlayer().getAvatar().getPosition().getX();
        int playerY = client.getPlayer().getAvatar().getPosition().getY();
        int rot = client.getPlayer().getAvatar().getBodyRotation();

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

            RoomModel model = client.getPlayer().getAvatar().getRoom().getModel();

            if(model.getDoorX() == posX && model.getDoorY() == posY) {
                this.sendChat("You can't push a user out of the room!", client);
                return;
            }

            user.getPlayer().getAvatar().setGoal(posX, posY);

            if(user.getPlayer().getAvatar().getPathfinder() == null) {
                user.getPlayer().getAvatar().setPathfinder();
            }

            LinkedList<Square> path = user.getPlayer().getAvatar().getPathfinder().makePath();
            user.getPlayer().getAvatar().unidle();
            user.getPlayer().getAvatar().setPath(path);
            user.getPlayer().getAvatar().isMoving = true;

            client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(
                    TalkMessageComposer.compose(client.getPlayer().getId(), "*pushes " + user.getPlayer().getData().getUsername() + "*", 0, 0)
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
