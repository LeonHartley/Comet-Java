package com.cometproject.server.game.rooms.types.mapping;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.items.RoomItemFloor;

public class TileInstance {
    private RoomMapping mappingInstance;
    private Position3D position;

    private double stackHeight = 0.0;

    private RoomEntityMovementNode movementNode;
    private RoomTileStatusType status;

    private boolean canStack;

    private int topItem = 0;

    public TileInstance(RoomMapping mappingInstance, Position3D position) {
        this.mappingInstance = mappingInstance;
        this.position = position;
        this.reload();
    }

    public void reload() {
        this.movementNode = RoomEntityMovementNode.OPEN;
        this.status = RoomTileStatusType.NONE;
        this.canStack = true;

        double highestHeight = 0d;
        int highestItem = 0;

        for (RoomItemFloor item : mappingInstance.getRoom().getItems().getItemsOnSquare(this.position.getX(), this.position.getY())) {
            if (item.getDefinition() == null)
                continue;

            final double totalHeight = item.getHeight() + item.getDefinition().getHeight();

            if(totalHeight > highestHeight) {
                highestHeight = totalHeight;
                highestItem = item.getId();
            }

            boolean isGate = item.getDefinition().getInteraction().equals("gate");

            if (!item.getDefinition().canWalk && !isGate) {
                movementNode = RoomEntityMovementNode.CLOSED;
            }

            switch (item.getDefinition().getInteraction().toLowerCase()) {
                case "bed":
                    status = RoomTileStatusType.LAY;
                    movementNode = RoomEntityMovementNode.END_OF_ROUTE;

                    if (item.getRotation() == 2 || item.getRotation() == 6) {
                        this.mappingInstance.getRedirectionGrid()[this.position.getX()][this.position.getY()] = new Position3D(item.getX(), this.position.getY());
                    } else if (item.getRotation() == 0 || item.getRotation() == 4) {
                        this.mappingInstance.getRedirectionGrid()[this.position.getX()][this.position.getY()] = new Position3D(this.position.getX(), item.getY());
                    }

                    break;

                case "gate":
                    movementNode = item.getExtraData().equals("1") ? RoomEntityMovementNode.OPEN : RoomEntityMovementNode.CLOSED;
                    break;

                case "onewaygate":
                    movementNode = RoomEntityMovementNode.CLOSED;
                    break;
            }

            if (item.getDefinition().canSit) {
                status = RoomTileStatusType.SIT;
                movementNode = RoomEntityMovementNode.END_OF_ROUTE;
            }

            if (item.getDefinition().getInteraction().equals("bed")) {
                status = RoomTileStatusType.LAY;
                movementNode = RoomEntityMovementNode.END_OF_ROUTE;
            }

            if (!item.getDefinition().canStack) {
                this.canStack = false;
            }
        }

        this.stackHeight = highestHeight;
        this.topItem = highestItem;
    }

    public RoomEntityMovementNode getMovementNode() {
        return this.movementNode;
    }

    public double getStackHeight() {
        return this.stackHeight;
    }

    public RoomTileStatusType getStatus() {
        return this.status;
    }

    public Position3D getPosition() {
        return this.position;
    }

    public boolean canStack() {
        return this.canStack;
    }

    public int getTopItem() {
        return this.topItem;
    }

    public void setTopItem(int topItem) {
        this.topItem = topItem;
    }
}
