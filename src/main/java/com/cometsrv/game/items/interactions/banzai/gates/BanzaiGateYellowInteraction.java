package com.cometsrv.game.items.interactions.banzai.gates;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.components.games.GameTeam;
import com.cometsrv.game.rooms.types.components.games.GameType;

public class BanzaiGateYellowInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, FloorItem item, Avatar avatar) {
        Room room = avatar.getRoom();

        if(room.getGame().getInstance() == null) {
            room.getGame().createNew(GameType.BANZAI);
        }

        if(room.getGame().getInstance().getType() != GameType.BANZAI) {
            return false;
        }

        int id = avatar.getPlayer().getId();

        if(room.getGame().getInstance().isTeamed(id)) {
            room.getGame().getInstance().removeFromTeam(id);
        }

        room.getGame().getInstance().getTeams().put(id, GameTeam.YELLOW);
        avatar.setGameTeam(GameTeam.YELLOW);

        return false;
    }

    @Override
    public boolean onInteract(int request, FloorItem item, Avatar avatar) {
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
        return false;
    }

    @Override
    public boolean requiresRights() {
        return true;
    }
}
