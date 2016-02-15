package com.cometproject.server.game.rooms.objects.items.types.floor.banzai;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import org.apache.commons.lang.StringUtils;


public class BanzaiTimerFloorItem extends RoomItemFloor {
    private String lastTime;

    public BanzaiTimerFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
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
                return false;
            }
        }

        if (requestData == 2) {
            int time = 0;

            if (!this.getExtraData().isEmpty() && StringUtils.isNumeric(this.getExtraData())) {
                time = Integer.parseInt(this.getExtraData());
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

            this.setExtraData(time + "");
            this.sendUpdate();
            this.saveData();
        } else {
            if(this.getExtraData().equals("0") && this.lastTime != null && !this.lastTime.isEmpty()) {
                this.setExtraData(this.lastTime);
            }

            int gameLength = Integer.parseInt(this.getExtraData());

            this.lastTime = this.getExtraData();

            if(gameLength == 0) return true;

            if (this.getRoom().getGame().getInstance() == null) {
                this.getRoom().getGame().createNew(GameType.BANZAI);
                this.getRoom().getGame().getInstance().startTimer(gameLength);
            }
        }

        return true;
    }

    @Override
    public void onPickup() {
        if (this.getRoom().getGame().getInstance() != null) {
            this.getRoom().getGame().getInstance().onGameEnds();
            this.getRoom().getGame().stop();
        }
    }

    @Override
    public String getDataObject() {
        return this.lastTime != null && !this.lastTime.isEmpty() ? this.lastTime : this.getExtraData();
    }
}
