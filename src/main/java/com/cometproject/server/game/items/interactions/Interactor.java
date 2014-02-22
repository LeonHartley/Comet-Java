package com.cometproject.server.game.items.interactions;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;

public abstract class Interactor {
    /*
     * State: user is walking on or off furni (true = on, false = off)
     * GenericRoomItem: the interacting item
     * Avatar: the interacting avatar
     */
    public abstract boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar);

    /*
     * RoomItem: the interacting item (most likely a football.. :P)
     * Avatar: the interacting avatar
     */
    public abstract boolean onPreWalk(RoomItem item, PlayerEntity avatar);

    /*
     * State: ???
     * GenericRoomItem: the interacting item
     * Avatar: the interacting avatar
     */
    public abstract boolean onInteract(int request, RoomItem item, PlayerEntity avatar);

    /*
     * GenericRoomItem: the item placed
     * Avatar: the avatar who placed the item
     * Room: the room the item was placed in
     */
    public abstract boolean onPlace(RoomItem item, PlayerEntity avatar, Room room);

    /*
     * GenericRoomItem: the item placed
     * Avatar: the avatar who picked up the item
     * Room: the room the item was picked up
     */
    public abstract boolean onPickup(RoomItem item, PlayerEntity avatar, Room room);

    /*
     * GenericRoomItem: the item which is ticking
     */
    public abstract boolean onTick(RoomItem item);

    public abstract boolean requiresRights();
}
