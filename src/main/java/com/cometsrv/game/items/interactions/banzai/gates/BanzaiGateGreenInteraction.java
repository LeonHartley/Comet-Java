package com.cometsrv.game.items.interactions.banzai.gates;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.items.RoomItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.components.games.GameTeam;
import com.cometsrv.game.rooms.types.components.games.GameType;

public class BanzaiGateGreenInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, Avatar avatar) {
        Room room = avatar.getRoom();

        if(room.getGame().getInstance() == null) {
            room.getGame().createNew(GameType.BANZAI);

            if(Comet.isDebugging)
                room.getGame().getInstance().startTimer(120);
        }

        if(room.getGame().getInstance().getType() != GameType.BANZAI) {
            return false;
        }

        int id = avatar.getPlayer().getId();

        if(room.getGame().getInstance().isTeamed(id)) {
            room.getGame().getInstance().removeFromTeam(id);
        }

        room.getGame().getInstance().getTeams().put(id, GameTeam.GREEN);
        avatar.setGameTeam(GameTeam.GREEN);

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
        return true;
    }
}
