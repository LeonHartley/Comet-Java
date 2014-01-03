package com.cometsrv.game.items.interactions.items;


import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.utilities.DistanceCalculator;

import java.util.Random;

public class WheelInteractor extends Interactor {
    @Override
    public boolean onWalk(boolean state, FloorItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, FloorItem item, Avatar avatar) {
        if (DistanceCalculator.tilesTouching(avatar.getPosition().getX(), avatar.getPosition().getY(), item.getX(), item.getY())) {
            avatar.moveTo(item.getX(), item.getY());
            return false;
        }

        if (!"-1".equals(item.getExtraData())) {
            item.setExtraData("-1");
            item.sendUpdate();
        }

        return false;
    }

    @Override
    public boolean onPlace(FloorItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(FloorItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(FloorItem item) {
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
