package com.cometsrv.game.items.interactions.items;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.items.RoomItem;
import com.cometsrv.game.rooms.types.Room;

public class TeleportInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar) {
        /*int pairId = GameEngine.getItems().getTeleportPartner(item.getId());

        if(pairId == 0) {
            return false;
        }

        Room room = avatar.getRoom();

        FloorItem partner = room.getItems().getFloorItem(pairId);

        if(partner == null) {
            // We'll have to find the item in db and get the room id?
            // TODO: find room where partner tele exists
            return false;
        }

        boolean flash = false;

        Position3D posInFront = item.squareInfront();

        if((avatar.getPosition().getX() != posInFront.getX() && avatar.getPosition().getY() != posInFront.getY())
                && !(avatar.getPosition().getX() == item.getX() && avatar.getPosition().getY() == item.getY())) {
            avatar.moveTo(posInFront.getX(), posInFront.getY());

            return false;
        }

        if(!item.getExtraData().equals("1")) {
            item.setExtraData("1");
        }

        if(avatar.getPosition().getX() != item.getX() && avatar.getPosition().getY() != item.getY()) {
            avatar.warpTo(item.getX(), item.getY());
            flash = true;
        }

        if(flash) {
            if(!item.getExtraData().equals("2")) {
                item.setExtraData("2");
            }
        } else {
            if(!item.getExtraData().equals("0")) {
                item.setExtraData("0");

            }
        }

        item.sendUpdate();*/

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
    public boolean onTick(RoomItem item) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
