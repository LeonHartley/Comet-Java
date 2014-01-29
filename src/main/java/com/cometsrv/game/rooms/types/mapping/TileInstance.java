package com.cometsrv.game.rooms.types.mapping;

import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.items.FloorItem;

public class TileInstance {
    private RoomMapping mappingInstance;
    private Position3D position;

    private double stackHeight = 0.0;

    private RoomEntityMovementNode movementNode = RoomEntityMovementNode.OPEN;
    private RoomTileStatusType status = RoomTileStatusType.NONE;

    public TileInstance(RoomMapping mappingInstance, Position3D position) {
        this.mappingInstance = mappingInstance;
        this.position = position;
        this.reload();
    }

    public void reload() {
        for(FloorItem item : mappingInstance.getRoom().getItems().getItemsOnSquare(this.position.getX(), this.position.getY())) {
            stackHeight += item.getHeight() + Math.round(item.getDefinition().getHeight());

            if(!item.getDefinition().canWalk) {
                this.movementNode = RoomEntityMovementNode.CLOSED;
            }

            switch (item.getDefinition().getInteraction().toLowerCase()) {
                case "sit":
                    status = RoomTileStatusType.SIT;
                    movementNode = RoomEntityMovementNode.END_OF_ROUTE;
                    break;
                case "bed":
                    status = RoomTileStatusType.LAY;
                    movementNode = RoomEntityMovementNode.END_OF_ROUTE;
                    break;

                case "gate":
                    movementNode = item.getExtraData().equals("1") ? RoomEntityMovementNode.OPEN : RoomEntityMovementNode.CLOSED;
                    break;

                case "sticky_pole":
                    this.mappingInstance.setGuestsPlaceStickies(true);
                    break;
            }
        }
    }

    public void addItem(FloorItem item) {
        stackHeight += item.getHeight() + Math.round(item.getDefinition().getHeight());
    }

    public void removeItem(FloorItem item) {
        this.stackHeight -= item.getHeight() + Math.round(item.getDefinition().getHeight());
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
}
