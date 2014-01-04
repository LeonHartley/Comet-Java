package com.cometsrv.game.items.interactions.banzai;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.items.RoomItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.components.games.banzai.BanzaiGame;

public class BanzaiPatchInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, Avatar avatar) {

        if(avatar.isTeamed()) {
            if(avatar.getRoom().getGame().getInstance() != null) {
                ((BanzaiGame)avatar.getRoom().getGame().getInstance()).captureTile(item.getX(), item.getY(), avatar.getTeam());

                avatar.getRoom().log.debug("Tile captured! x: " + item.getX() + ", y: " + item.getY());
            }
        }

        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, Avatar avatar) {
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
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
