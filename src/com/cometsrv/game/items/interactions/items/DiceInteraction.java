package com.cometsrv.game.items.interactions.items;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;

public class DiceInteraction extends Interactor {

    @Override
    public boolean onWalk(boolean state, FloorItem item, Avatar avatar) {
        return true;
    }

    @Override
    public boolean onInteract(int request, FloorItem item, Avatar avatar) {
        //TODO: this

        return true;
    }

}
