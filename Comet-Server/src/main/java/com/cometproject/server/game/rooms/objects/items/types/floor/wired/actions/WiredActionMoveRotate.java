package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.DiceFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;

import java.util.Random;


public class WiredActionMoveRotate extends WiredActionItem {
    private static final int PARAM_MOVEMENT = 0;
    private static final int PARAM_ROTATION = 1;

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
    public WiredActionMoveRotate(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 4;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.hasTicks()) return false;

        if (this.getWiredData().getDelay() >= 1) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
        } else {
            this.onTickComplete();
        }

        return true;
    }

    @Override
    public void onTickComplete() {
        if (this.getWiredData().getParams().size() != 2) {
            return;
        }

        final int movement = this.getWiredData().getParams().get(PARAM_MOVEMENT);
        final int rotation = this.getWiredData().getParams().get(PARAM_ROTATION);

        synchronized (this.getWiredData().getSelectedIds()) {
            for (long itemId : this.getWiredData().getSelectedIds()) {
                RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

                if (floorItem == null || floorItem instanceof DiceFloorItem) continue;

                final Position currentPosition = floorItem.getPosition().copy();
                final Position newPosition = this.handleMovement(currentPosition.copy(), movement);
                final int newRotation = this.handleRotation(floorItem.getRotation(), rotation);
                final boolean rotationChanged = newRotation != floorItem.getRotation();

                if (this.getRoom().getItems().moveFloorItem(floorItem.getId(), newPosition, newRotation, true)) {
                    if (!rotationChanged)
                        this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(currentPosition, newPosition, 0, 0, floorItem.getVirtualId()));
                    else
                        this.getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(floorItem));
                }

                floorItem.save();
            }
        }
    }

    private final Random random = new Random();

    private Position handleMovement(Position point, int movementType) {
        switch (movementType) {
            case 0:
                return point;

            case 1:
                // Random
                int movement = random.nextInt((4 - 1) + 1 + 1);

                if (movement == 1) {
                    point = handleMovement(point, 4);
                } else if (movement == 2) {
                    point = handleMovement(point, 5);
                } else if (movement == 3) {
                    point = handleMovement(point, 6);
                } else {
                    point = handleMovement(point, 7);
                }
                break;

            case 2:
                // Left right
                int i = random.nextInt((2 - 1) + 1 + 1);

                if (i == 1) {
                    point = handleMovement(point, 7);
                } else {
                    point = handleMovement(point, 5);
                }
                break;

            case 3:
                // Up down
                int j = random.nextInt((2 - 1) + 1 + 1);

                if (j == 1) {
                    point = handleMovement(point, 4);
                } else {
                    point = handleMovement(point, 6);
                }
                break;

            case 4:
                // Up
                point.setY(point.getY() - 1);
                break;

            case 5:
                // Right
                point.setX(point.getX() + 1);
                break;

            case 6:
                // Down
                point.setY(point.getY() + 1);
                break;

            case 7:
                // Left
                point.setX(point.getX() - 1);
                break;
        }

        return point;
    }

    private int handleRotation(int rotation, int rotationType) {
        switch (rotationType) {
            case 0:
                return rotation;

            case 1:
                // Clockwise
                rotation = rotation + 2;

                if (rotation > 6)
                    rotation = 0;
                break;

            case 2:
                // Counter clockwise
                rotation = rotation - 2;

                if (rotation > 6)
                    rotation = 6;
                break;

            case 3:
                // Random
                int i = random.nextInt((2 - 1) + 1 + 1);

                if (i == 1) {
                    rotation = handleRotation(rotation, 1);
                } else {
                    rotation = handleRotation(rotation, 2);
                }
                break;

        }

        return rotation;
    }
}
