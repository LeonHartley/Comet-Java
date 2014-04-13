package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

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

            if(!client.getPlayer().getEntity().isOverriden())
                client.getPlayer().getEntity().moveTo(goalX, goalY);
        } catch(Exception e) {
            client.getLogger().error("Error while finding path", e);
        }
    }
}
