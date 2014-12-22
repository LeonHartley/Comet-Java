package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;


public class WalkMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int goalX = msg.readInt();
        int goalY = msg.readInt();

        try {
            if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().hasAttribute("warp")) {
                // User not in room!
                return;
            }

            if (goalX == client.getPlayer().getEntity().getPosition().getX() && goalY == client.getPlayer().getEntity().getPosition().getY()) {
                return;
            }

            if (client.getPlayer().getEntity().hasAttribute("teleport")) {
                List<Square> squares = new ArrayList<>();
                squares.add(new Square(goalX, goalY));

                if (client.getPlayer().getEntity().getMountedEntity() != null) {
                    client.getPlayer().getEntity().getMountedEntity().setWalkingPath(squares);
                    client.getPlayer().getEntity().getMountedEntity().setWalkingGoal(goalX, goalY);
                }

                client.getPlayer().getEntity().setWalkingPath(squares);
                client.getPlayer().getEntity().setWalkingGoal(goalX, goalY);
                return;
            }

            if (client.getPlayer().getEntity().canWalk() && !client.getPlayer().getEntity().isOverriden() && client.getPlayer().getEntity().isVisible()) {
                client.getPlayer().getEntity().moveTo(goalX, goalY);

                if (client.getPlayer().getEntity().getMountedEntity() != null) {
                    client.getPlayer().getEntity().getMountedEntity().moveTo(goalX, goalY);
                }
            }
        } catch (Exception e) {
            client.getLogger().error("Error while finding path", e);
        }
    }
}
