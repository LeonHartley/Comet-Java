package com.cometsrv.game.rooms.types.mapping;

import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomModel;
import com.cometsrv.game.rooms.types.tiles.RoomTileState;

import java.util.AbstractQueue;
import java.util.List;

public class RoomMapping {
    private final Room room;
    private final RoomModel model;

    private volatile Position3D[][] redirectGrid;
    private volatile RoomTileStatus[][] statusGrid;
    private volatile RoomEntityMovementNode[][] movementNodes;

    private volatile double[][] stackHeight;
    private volatile double[][] topStackHeight;

    public RoomMapping(Room roomInstance, RoomModel roomModel) {
        this.room = roomInstance;
        this.model = roomModel;
    }

    public boolean isValidRotation(int rotation) {
        return (rotation == 0 || rotation == 2 || rotation == 4 || rotation == 6);
    }

    public double getStepHeight(Position3D position) {
        if (!isValidPosition(position)) {
            return 0.0;
        }

        RoomTileStatus tileStatus = this.statusGrid[position.getX()][position.getX()];
        double height = this.stackHeight[position.getX()][position.getY()];

        if (tileStatus == null) {
            return 0.0;
        }

        if (tileStatus.getInteractionHeight() >= 0) {
            height -= tileStatus.getInteractionHeight();
        }

        return height;
    }

    public boolean canStepUpwards(double height0, double height1) {
        double stepHeight = (height0 - height1);

        if (stepHeight > 1.5) {
            return false;
        }

        return true;
    }

    public Position3D getRedirectedPosition(Position3D position) {
        return (this.redirectGrid[position.getX()][position.getY()] != null ? this.redirectGrid[position.getX()][position.getY()] : position);
    }

    public boolean positionHasUser(Position3D position3D) {
        return this.room.getEntities().getEntitiesAt(position3D.getX(), position3D.getY()).size() > 0;
    }

    public boolean isValidPosition(Position3D position) {
        return (position.getX() >= 0 && position.getY() >= 0 && position.getX() < this.model.getSizeX() && position.getY() < this.model.getSizeY());
    }

    public boolean isValidStep(Position3D from, Position3D to, boolean lastStep) {
        // Check we are within the bounds of the room
        if (!isValidPosition(to)) {
            return false;
        }

        if (this.model.getSquareState()[to.getX()][to.getY()] == RoomTileState.INVALID
                || positionHasUser(to)
                || movementNodes[to.getX()][to.getY()] == RoomEntityMovementNode.CLOSED
                || movementNodes[to.getX()][to.getY()] == RoomEntityMovementNode.END_OF_ROUTE && !lastStep) {
            return false;
        }

        // Is the step a door and is it the last step? (if its not then we don't want to walk into the door to get around furniture)
        if (to.getX() == this.model.getDoorX() && to.getY() == this.model.getDoorY() && !lastStep) {
            return false;
        }

        // Is the next step too high for us to walk to?
        if (!canStepUpwards(getStepHeight(to), getStepHeight(from))) {
            return false;
        }

        return true;
    }

    public double calculateItemPlacementHeight(FloorItem item, List<AffectedTile> affectedTiles, boolean rotateOnly) {
        double rootFloorHeight = -1;
        double heighestStack = 0;

        int sizeX = this.model.getSizeX();
        int sizeY = this.model.getSizeY();

        double[][] tmpStackHeights = new double[sizeX][sizeY];

        for (AffectedTile tile : affectedTiles) {
            // Check valid position & door position
            if (!isValidPosition(new Position3D(tile.x, tile.y, 0)) || (tile.x == this.model.getDoorX() && tile.y == this.model.getDoorY())) {
                return -1;
            }

            // Calculate first the 'root' floor height (stairs etc)
            if (rootFloorHeight == -1) {
                rootFloorHeight = this.model.getSquareHeight()[tile.x][tile.y];
            }

            if (rootFloorHeight != this.model.getSquareHeight()[tile.x][tile.y]) {
                return -1;
            }

            tmpStackHeights[tile.x][tile.y] = rootFloorHeight;
            heighestStack = rootFloorHeight;

            boolean canRotateIntoEntity = (item.getDefinition().getInteraction().toLowerCase() == "bed"
                    || item.getDefinition().getInteraction().toLowerCase() == "seat"
                    || item.getDefinition().getInteraction().toLowerCase() == "roller"
                    || item.getDefinition().getInteraction().toLowerCase() == "teleporter");

            if ((!rotateOnly || !canRotateIntoEntity) && this.room.getEntities().getEntitiesAt(tile.x, tile.y).size() > 0) {
                return -1;
            }
        }

        for (FloorItem stackItem : this.room.getItems().getFloorItems()) {
            if (stackItem.getId() == item.getId()) {
                continue;
            }

            Position3D matchedTile;
            List<AffectedTile> itemTiles = AffectedTile.getAffectedTilesAt(stackItem.getDefinition().getLength(), stackItem.getDefinition().getWidth(), stackItem.getX(), stackItem.getY(), stackItem.getRotation());

            for (AffectedTile affectedTile : affectedTiles) {
                for (AffectedTile itemTile : itemTiles) {
                    if (itemTile.x == affectedTile.x && itemTile.y == affectedTile.y) {
                        matchedTile = new Position3D(affectedTile.x, affectedTile.y, 0);

                        if (stackItem.getDefinition().getInteraction() == "roller" && (item.getDefinition().getLength() != 1 || item.getDefinition().getWidth() != 1)) {
                            return -1;
                        }

                        double itemTotalHeight = stackItem.getHeight() + stackItem.getDefinition().getHeight();

                        if (itemTotalHeight >= tmpStackHeights[matchedTile.getX()][matchedTile.getY()]) {
                            tmpStackHeights[matchedTile.getX()][matchedTile.getY()] = itemTotalHeight;
                        }

                        if (itemTotalHeight >= heighestStack) {
                            heighestStack = itemTotalHeight;
                        }
                    }
                }
            }
        }

        // Stack height limits
        if ((heighestStack + item.getDefinition().getHeight()) >= 12) {
            return -1;
        }

        return heighestStack;
    }

    public void regenerate() {
        int sizeX = this.model.getSizeX();
        int sizeY = this.model.getSizeY();

        Position3D[][] redirectGrid = new Position3D[sizeX][sizeY];
        RoomTileStatus[][] statusGrid = new RoomTileStatus[sizeX][sizeY];
        RoomEntityMovementNode[][] movementNodes = new RoomEntityMovementNode[sizeX][sizeY];
        double[][] stackHeight = new double[sizeX][sizeY];
        double[][] topStackHeight = new double [sizeX][sizeY];

        for (int y = 0; y < sizeY; y++)
        {
            for (int x = 0; x < sizeX; x++)
            {
                movementNodes[x][y] = RoomEntityMovementNode.OPEN;
                stackHeight[x][y] = this.room.getModel().getSquareHeight()[x][y];
                topStackHeight[x][y] = 0;
            }
        }

        AbstractQueue<FloorItem> floorItems = this.room.getItems().getFloorItems();

        for (FloorItem item : floorItems) {
            int itemX = item.getX();
            int itemY = item.getY();
            int itemRotation = item.getRotation();

            double totalStackHeight = item.getHeight() + Math.round(item.getDefinition().getHeight());
            List<AffectedTile> affectedTiles = AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), itemX, itemY, itemRotation);

            RoomTileStatus status = null;
            RoomEntityMovementNode movementNode = RoomEntityMovementNode.CLOSED;

            switch (item.getDefinition().getInteraction().toLowerCase()) {
                case "sit":
                    status = new RoomTileStatus(RoomTileStatusType.SIT, 0, itemX, itemY, itemRotation, totalStackHeight);
                    movementNode = RoomEntityMovementNode.END_OF_ROUTE;
                    break;
                case "bed":
                    status = new RoomTileStatus(RoomTileStatusType.LAY, 0, itemX, itemY, itemRotation, totalStackHeight);
                    movementNode = RoomEntityMovementNode.END_OF_ROUTE;
                    break;

                case "gate":
                    movementNode = item.getExtraData().equals("1") ? RoomEntityMovementNode.OPEN : RoomEntityMovementNode.CLOSED;
                    break;
            }

            for (AffectedTile tile : affectedTiles) {
                if (totalStackHeight >= this.stackHeight[tile.x][tile.y]) {
                    stackHeight[tile.x][tile.y] = totalStackHeight;
                    topStackHeight[tile.x][tile.y] = item.getHeight();
                    movementNodes[tile.x][tile.y] = movementNode;
                    statusGrid[tile.x][tile.y] = status;

                    if (item.getDefinition().getInteraction().toLowerCase() == "bed") {
                        if (itemRotation == 2 || itemRotation == 6) {
                            redirectGrid[tile.x][tile.y] = new Position3D(itemX, tile.y, 0);
                        } else if (itemRotation == 0 || itemRotation == 4) {
                            redirectGrid[tile.x][tile.y] = new Position3D(tile.x, itemY, 0);
                        }
                    }
                }
            }
        }

        this.redirectGrid = redirectGrid;
        this.statusGrid = statusGrid;
        this.movementNodes = movementNodes;
        this.stackHeight = stackHeight;
        this.topStackHeight = topStackHeight;
    }
}
