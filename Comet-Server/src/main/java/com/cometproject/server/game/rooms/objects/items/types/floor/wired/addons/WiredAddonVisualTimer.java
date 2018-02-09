package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerGameEnds;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerGameStarts;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang.StringUtils;

public class WiredAddonVisualTimer extends RoomItemFloor {

    private boolean isStarted = false;

    public WiredAddonVisualTimer(RoomItemData itemData, Room room) {
        super(itemData, room);
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
                return false;
            }
        }

        if (requestData == 2) {
            int time = 0;

            if (!this.getItemData().getData().isEmpty() && StringUtils.isNumeric(this.getItemData().getData())) {
                time = Integer.parseInt(this.getItemData().getData());
            }

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

            this.getItemData().setData(time + "");
            this.sendUpdate();
        } else {
            if (this.isStarted) {
                this.isStarted = false;
//                this.getItemData().setData("0");
//                this.sendUpdate();

                return true;
            }

            // Start time.
            this.isStarted = true;

            WiredTriggerGameStarts.executeTriggers(this.getRoom());
            this.setTicks(RoomItemFactory.getProcessTime(1));
        }

        return true;
    }

    @Override
    public void onTickComplete() {
        if (!this.isStarted) {
            return;
        }

        if (this.getItemData().getData().isEmpty() || this.getItemData().getData().equals("0")) {
            this.isStarted = false;
            return;
        }

        int timeLength = Integer.parseInt(this.getItemData().getData());

        if (timeLength - 1 >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(1.0));
        } else {
            WiredTriggerGameEnds.executeTriggers(this.getRoom());
            this.isStarted = false;
        }

        this.getItemData().setData("" + (timeLength - 1));
        this.sendUpdate();
        this.saveData();
    }
}
