package com.cometproject.server.game.rooms.objects.items.types.floor.football;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerGameEnds;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerGameStarts;
import com.cometproject.server.game.rooms.types.Room;


public class FootballTimerFloorItem extends RoomItemFloor {
    private int time = 0;

    public FootballTimerFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }


    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTriggered) {
        if (!isWiredTriggered) {
            if (!(entity instanceof PlayerEntity)) {
                return false;
            }

            PlayerEntity pEntity = (PlayerEntity) entity;

            if (!pEntity.getRoom().getRights().hasRights(pEntity.getPlayerId())
                    && !pEntity.getPlayer().getPermissions().getRank().roomFullControl()) {
                return true;
            }
        }

        if (requestData == 1) {
            int time = Integer.parseInt(this.getExtraData());

            if (time == 0 || time == 30 || time == 60 || time == 120 || time == 180 || time == 300 || time == 600) {
                switch (time) {
                    default:
                        time = 0;
                        break;
                    case 0:
                        time = 30;
                        break;
                    case 30:
                        time = 60;
                        break;
                    case 60:
                        time = 120;
                        break;
                    case 120:
                        time = 180;
                        break;
                    case 180:
                        time = 300;
                        break;
                    case 300:
                        time = 600;
                        break;
                }
            } else {
                time = 0;
            }

            this.time = time;
            this.setExtraData(this.time + "");
            this.sendUpdate();
        } else {
            // Tell the room we have an active football game.
            this.getRoom().setAttribute("football", true);

            for (RoomItemFloor scoreItem : this.getRoom().getItems().getByClass(FootballScoreFloorItem.class)) {
                ((FootballScoreFloorItem) scoreItem).reset();
            }

            WiredTriggerGameStarts.executeTriggers(this.getRoom());
            this.setTicks(RoomItemFactory.getProcessTime(1.0));
        }
        return true;
    }

    @Override
    public void onTickComplete() {
        if (this.time > 0) {
            this.time--;

            this.setExtraData(this.time + "");
            this.sendUpdate();

            this.setTicks(RoomItemFactory.getProcessTime(1.0));
        } else {
            if (this.getRoom().hasAttribute("football")) {

                WiredTriggerGameEnds.executeTriggers(this.getRoom());
                this.getRoom().removeAttribute("football");
            }
        }
    }
}
