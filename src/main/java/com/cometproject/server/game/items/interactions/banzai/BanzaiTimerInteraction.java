package com.cometproject.server.game.items.interactions.banzai;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;

public class BanzaiTimerInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        switch (request) {
            case 2:
                int time = Integer.parseInt(item.getExtraData());

                if(time == 0 || time == 30 || time == 60 || time == 120 || time == 180 || time == 300 || time == 600) {
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

                item.setExtraData(time + "");
                item.sendUpdate();
                break;

            case 1:
                int gameLength = Integer.parseInt(item.getExtraData());

                if (((FloorItem) item).getRoom().getGame().getInstance() == null) {
                    ((FloorItem) item).getRoom().getGame().createNew(GameType.BANZAI);

                    ((FloorItem) item).getRoom().getGame().getInstance().startTimer(gameLength);
                }
                break;

            default:
                // idk
                break;
        }

        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item, PlayerEntity avatar, int updateState) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
