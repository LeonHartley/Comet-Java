package com.cometsrv.game.items.interactions;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;

public abstract class Interactor {
    /*
     * State: user is walking on or off furni (true = on, false = off)
     * GenericRoomItem: the interacting item
     * Avatar: the interacting avatar
     */
    public abstract boolean onWalk(boolean state, FloorItem item, Avatar avatar);

    /*
     * State: ???
     * GenericRoomItem: the interacting item
     * Avatar: the interacting avatar
     */
    public abstract boolean onInteract(int request, FloorItem item, Avatar avatar);

    /*
     * GenericRoomItem: the item placed
     * Avatar: the avatar who placed the item
     * Room: the room the item was placed in
     */
    public abstract boolean onPlace(FloorItem item, Avatar avatar, Room room);

    /*
     * GenericRoomItem: the item placed
     * Avatar: the avatar who picked up the item
     * Room: the room the item was picked up
     */
    public abstract boolean onPickup(FloorItem item, Avatar avatar, Room room);

    /*
     * GenericRoomItem: the item which is ticking
     */
    public abstract boolean onTick(FloorItem item);

    public abstract boolean requiresRights();
}
