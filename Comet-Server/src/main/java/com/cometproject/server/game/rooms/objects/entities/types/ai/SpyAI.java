package com.cometproject.server.game.rooms.objects.entities.types.ai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.entities.types.data.types.SpyBotData;

public class SpyAI extends AbstractBotAI {

    private boolean hasSaidYes = false;

    public SpyAI(GenericEntity entity) {
        super(entity);
    }

    @Override
    public boolean onPlayerEnter(PlayerEntity playerEntity) {
        // TODO: log entry if owner isn't in the room.

        if (playerEntity.getPlayerId() != this.getBotEntity().getData().getOwnerId()) {
            if (!((SpyBotData) this.getBotEntity().getDataObject()).getVisitors().contains(playerEntity.getUsername())) {
                ((SpyBotData) this.getBotEntity().getDataObject()).getVisitors().add(playerEntity.getUsername());
            }
        } else {
            if(((SpyBotData) this.getBotEntity().getDataObject()).getVisitors().size() == 0) {
                this.getBotEntity().say("There have been no visitors while you've been away!!!");
                this.hasSaidYes = true;
            } else {
                this.getBotEntity().say("Nice to see you Sir! Please say yes if you'd like me to tell who have visited room while you've been gone.");
                this.hasSaidYes = false;
            }
        }

        return false;
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {
        if (this.hasSaidYes) {
            return false;
        }

        if (entity.getPlayerId() == this.getBotEntity().getData().getOwnerId()) {
            if (message.equals("yes")) {
                String stillIn = "";
                String left = "";

                for (String username : ((SpyBotData) this.getBotEntity().getDataObject()).getVisitors()) {
                    boolean isLast = ((SpyBotData) this.getBotEntity().getDataObject()).getVisitors().indexOf(username) == (((SpyBotData) this.getBotEntity().getDataObject()).getVisitors().size() - 1);

                    if (this.getBotEntity().getRoom().getEntities().getEntityByName(username, RoomEntityType.PLAYER) != null) {
                        if (isLast) {
                            stillIn += username + (stillIn.equals("") ? " is still in the room" : " are still in the room");
                        } else {
                            stillIn += username + ", ";
                        }
                    } else {
                        if (isLast) {
                            left += username + (stillIn.equals("") ? " has left" : " have left");
                        } else {
                            left += username + ", ";
                        }
                    }
                }

                if(!left.equals("")) {
                    this.getBotEntity().say(left);
                }

                if(!stillIn.equals("")) {
                    this.getBotEntity().say(stillIn);
                }

                ((SpyBotData) this.getBotEntity().getDataObject()).getVisitors().clear();
                this.hasSaidYes = true;
            }
        }
        return false;
    }

    @Override
    public boolean onPlayerLeave(PlayerEntity entity) {
        if (entity.getPlayerId() == this.getBotEntity().getData().getOwnerId()) {
            this.hasSaidYes = false;
        }

        return false;
    }
}
