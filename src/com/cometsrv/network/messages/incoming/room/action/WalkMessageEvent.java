package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.game.rooms.avatars.pathfinding.Square;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

import java.util.LinkedList;

public class WalkMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int goalX = msg.readInt();
        int goalY = msg.readInt();

        try {
            if(client.getPlayer().getAvatar().getPathfinder() == null)
                client.getPlayer().getAvatar().setPathfinder();

            if(goalX == client.getPlayer().getAvatar().getPosition().getX() && goalY == client.getPlayer().getAvatar().getPosition().getY()) {
                return;
            }

            client.getPlayer().getAvatar().setGoal(goalX, goalY);

            LinkedList<Square> path = client.getPlayer().getAvatar().getPathfinder().makePath();

            if(path == null) {
                return;
            }

            client.getPlayer().getAvatar().unidle();

            client.getPlayer().getAvatar().setPath(path);
            client.getPlayer().getAvatar().isMoving = true;
        } catch(Exception e) {
            client.getLogger().error("Error while finding path", e);
        }
    }
}
