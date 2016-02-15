package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

import java.util.LinkedList;
import java.util.List;


public class WalkMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int goalX = msg.readInt();
        int goalY = msg.readInt();

        try {
            if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().hasAttribute("warp")) {
                // User not in room!
                return;
            }

            PlayerEntity entity = client.getPlayer().getEntity();

            if(!entity.isVisible()) return;

            if (goalX == entity.getPosition().getX() && goalY == entity.getPosition().getY()) {
                return;
            }

            if (entity.hasAttribute("teleport")) {
                List<Square> squares = new LinkedList<>();
                squares.add(new Square(goalX, goalY));

                if (entity.getMountedEntity() != null) {
                    entity.getMountedEntity().setWalkingPath(squares);
                    entity.getMountedEntity().setWalkingGoal(goalX, goalY);
                }

                entity.setWalkingPath(squares);
                entity.setWalkingGoal(goalX, goalY);
                return;
            }

            if (entity.canWalk() && !entity.isOverriden() && entity.isVisible()) {
                entity.moveTo(goalX, goalY);
            }
        } catch (Exception e) {
            client.getLogger().error("Error while finding path", e);
        }
    }
}
