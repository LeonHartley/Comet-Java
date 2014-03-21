package com.cometproject.server.game.rooms.types.mapping;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.items.FloorItem;

public class TileInstance {
    private RoomMapping mappingInstance;
    private Position3D position;

    private double stackHeight = 0.0;

    private RoomEntityMovementNode movementNode;
    private RoomTileStatusType status;

    private boolean canStack;

    public TileInstance(RoomMapping mappingInstance, Position3D position) {
        this.mappingInstance = mappingInstance;
        this.position = position;
        this.reload();
    }

    public void reload() {
        this.movementNode = RoomEntityMovementNode.OPEN;
        this.stackHeight = 0.0;
        this.status = RoomTileStatusType.NONE;
        this.canStack = true;

        for(FloorItem item : mappingInstance.getRoom().getItems().getItemsOnSquare(this.position.getX(), this.position.getY())) {
            boolean isGate = item.getDefinition().getInteraction().equals("gate");
            stackHeight += item.getHeight() + Math.round(item.getDefinition().getHeight());

            if(!item.getDefinition().canWalk && !isGate) {
                movementNode = RoomEntityMovementNode.CLOSED;
            }


            switch (item.getDefinition().getInteraction().toLowerCase()) {
                case "bed":
                    status = RoomTileStatusType.LAY;
                    movementNode = RoomEntityMovementNode.END_OF_ROUTE;
                    break;

                case "gate":
                    movementNode = item.getExtraData().equals("1") ? RoomEntityMovementNode.OPEN : RoomEntityMovementNode.CLOSED;
                    break;
            }

            if(item.getDefinition().canSit) {
                status = RoomTileStatusType.SIT;
                movementNode = RoomEntityMovementNode.END_OF_ROUTE;
            }

            if (item.getDefinition().getInteraction().equals("bed")) {
                status = RoomTileStatusType.LAY;
                movementNode = RoomEntityMovementNode.END_OF_ROUTE;
            }

            if(!item.getDefinition().canStack) {
                this.canStack = false;
            }
        }
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
}
