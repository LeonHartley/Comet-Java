package com.cometsrv.game.items.interactions.banzai.gates;

import com.cometsrv.game.items.interactions.InteractionState;
import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.components.games.GameTeam;
import com.cometsrv.game.rooms.types.components.games.GameType;

public class BanzaiGateBlueInteraction extends Interactor {
    @Override
    public InteractionState onWalk(boolean state, FloorItem item, Avatar avatar) {
        Room room = avatar.getRoom();

        if(room.getGame().getInstance() == null) {
            room.getGame().createNew(GameType.BANZAI);
        }

        if(room.getGame().getInstance().getType() != GameType.BANZAI) {
            return InteractionState.FINISHED;
        }

        int id = avatar.getPlayer().getId();

        if(room.getGame().getInstance().isTeamed(id)) {
            room.getGame().getInstance().removeFromTeam(id);
        }

        room.getGame().getInstance().getTeams().put(id, GameTeam.BLUE);
        avatar.setGameTeam(GameTeam.BLUE);

        return InteractionState.FINISHED;
    }

    @Override
    public InteractionState onInteract(int request, FloorItem item, Avatar avatar) {
        return InteractionState.FINISHED;
    }

    @Override
    public InteractionState onPlace(FloorItem item, Avatar avatar) {
        return InteractionState.FINISHED;
    }

    @Override
    public InteractionState onPickup(FloorItem item, Avatar avatar) {
        return InteractionState.FINISHED;
    }

    @Override
    public InteractionState onTick(FloorItem item, Avatar avatar) {
        return InteractionState.FINISHED;
    }

    @Override
    public boolean requiresRights() {
        return true;
    }
}
