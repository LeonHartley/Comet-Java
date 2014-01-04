package com.cometsrv.game.items.interactions;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.items.RoomItem;
import com.cometsrv.game.rooms.types.Room;

public abstract class Interactor {
    /*
     * State: user is walking on or off furni (true = on, false = off)
     * GenericRoomItem: the interacting item
     * Avatar: the interacting avatar
     */
    public abstract boolean onWalk(boolean state, RoomItem item, Avatar avatar);

    /*
     * State: ???
     * GenericRoomItem: the interacting item
     * Avatar: the interacting avatar
     */
    public abstract boolean onInteract(int request, RoomItem item, Avatar avatar);

    /*
     * GenericRoomItem: the item placed
     * Avatar: the avatar who placed the item
     * Room: the room the item was placed in
     */
    public abstract boolean onPlace(RoomItem item, Avatar avatar, Room room);

    /*
     * GenericRoomItem: the item placed
     * Avatar: the avatar who picked up the item
     * Room: the room the item was picked up
     */
    public abstract boolean onPickup(RoomItem item, Avatar avatar, Room room);

    /*
     * GenericRoomItem: the item which is ticking
     */
    public abstract boolean onTick(RoomItem item);

    public abstract boolean requiresRights();
}
