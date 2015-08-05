package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class SitMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer().getEntity() != null) {
            PlayerEntity playerEntity = client.getPlayer().getEntity();

            if(!client.getPlayer().getEntity().isVisible()) {
                return;
            }

            if (!playerEntity.hasStatus(RoomEntityStatus.SIT)) {
                double height = 0.5;

                //for (RoomItemFloor roomItemFloor : playerEntity.getRoom().getItems().getItemsOnSquare(playerEntity.getPosition().getX(), playerEntity.getPosition().getY())) {
                //    height += roomItemFloor.getHeight();
                //}

                int rotation = playerEntity.getBodyRotation();

                switch (rotation) {
                    case 1: {
                        rotation++;
                        break;
                    }
                    case 3: {
                        rotation++;
                        break;
                    }
                    case 5: {
                        rotation++;
                    }
                }

                playerEntity.addStatus(RoomEntityStatus.SIT, String.valueOf(height));
                playerEntity.setBodyRotation(rotation);
                playerEntity.markNeedsUpdate();
            }
        }
    }
}
