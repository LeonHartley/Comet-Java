package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class WalkMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int goalX = msg.readInt();
        int goalY = msg.readInt();

        try {
            /*if(client.getPlayer().getEntity().getPathfinder() == null)
                client.getPlayer().getEntity().setPathfinder();*/

            if(goalX == client.getPlayer().getEntity().getPosition().getX() && goalY == client.getPlayer().getEntity().getPosition().getY()) {
                return;
            }

            client.getPlayer().getEntity().moveTo(goalX, goalY);
        } catch(Exception e) {
            client.getLogger().error("Error while finding path", e);
        }
    }
}
