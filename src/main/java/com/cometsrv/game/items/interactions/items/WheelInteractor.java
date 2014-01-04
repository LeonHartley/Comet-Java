package com.cometsrv.game.items.interactions.items;


import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.items.RoomItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.utilities.DistanceCalculator;

import java.util.Random;

public class WheelInteractor extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, Avatar avatar) {
        if (!item.touching(avatar)) {
            //avatar.moveTo(item.getX(), item.getY());
            // CANNOT GET POSITION OF WHEEL FROM GETX / GETY!!!
            return false;
        }

        /*if (!"-1".equals(item.getExtraData())) {
            item.setExtraData("-1");
            item.sendUpdate();
        }*/

        int wheelPos = new Random().nextInt(10) + 1;

        item.setExtraData(Integer.toString(wheelPos));
        item.sendUpdate();

        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item) {
        int wheelPos = new Random().nextInt(10) + 1;

        item.setExtraData(Integer.toString(wheelPos));
        item.sendUpdate();

        return true;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
