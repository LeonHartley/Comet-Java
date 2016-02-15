package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.DiceFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;

import java.util.Random;

public class WiredActionMoveToDirection extends WiredActionItem {
    private static final int PARAM_START_DIR = 0;
    private static final int PARAM_ACTION_WHEN_BLOCKED = 1;

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionMoveToDirection(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 13;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.getWiredData().getParams().size() != 2) {
            return false;
        }

        final int startDir = this.getWiredData().getParams().get(PARAM_START_DIR);
        final int actionWhenBlocked = this.getWiredData().getParams().get(PARAM_ACTION_WHEN_BLOCKED);

        synchronized (this.getWiredData().getSelectedIds()) {
            for (long itemId : this.getWiredData().getSelectedIds()) {
                RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

                if (floorItem == null || floorItem instanceof DiceFloorItem) continue;

                final Position currentPosition = floorItem.getPosition().copy();
                final Position newPosition = this.handleMovement(currentPosition.copy(), startDir);
                final int newRotation = this.handleRotation(floorItem.getRotation(), rotation);
                final boolean rotationChanged = newRotation != floorItem.getRotation();

                if (this.getRoom().getItems().moveFloorItem(floorItem.getId(), newPosition, newRotation, true)) {
                    if (!rotationChanged)
                        this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(currentPosition, newPosition, 0, 0, floorItem.getVirtualId()));
                    else
                        this.getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(floorItem));
                } else {
                    // we need to do the "action when blocked" thingy.
                }

                floorItem.save();
            }
        }

        return false;
    }

    private final Random random = new Random();

    private Position handleMovement(Position point, int movementType) {
        // TODO: this.
        return point;
    }

    private int handleRotation(int rotation, int rotationType) {
        // TODO: this.

        return rotation;
    }
}
