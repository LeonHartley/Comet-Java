package com.cometsrv.game.items.interactions;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;

public abstract class Interactor {
    /*
     * State: user is walking on or off furni (true = on, false = off)
     * Item: the interacting item
     * Avatar: the interacting avatar
     */
    public abstract boolean onWalk(boolean state, FloorItem item, Avatar avatar);

    /*
     * State: ???
     * Item: the interacting item
     * Avatar: the interacting avatar
     */
    public abstract boolean onInteract(int request, FloorItem item, Avatar avatar);

    public abstract boolean requiresRights();
}
