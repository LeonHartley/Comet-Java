package com.cometproject.server.game.rooms.objects.items.types.floor.football;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerGameEnds;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerGameStarts;
import com.cometproject.server.game.rooms.types.Room;


public class FootballTimerFloorItem extends RoomItemFloor {
    private int time = 0;

    public FootballTimerFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void sendUpdate() {
        this.time = Integer.parseInt(this.getItemData().getData());

        super.sendUpdate();
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

        int time = Integer.parseInt(this.getItemData().getData());
        this.time = time;

        if (requestData != 1) {
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
            this.getItemData().setData(this.time + "");
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

            this.getItemData().setData(this.time + "");
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
