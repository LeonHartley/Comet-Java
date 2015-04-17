package com.cometproject.server.game.rooms.types.mapping;

import com.cometproject.server.game.rooms.models.RoomModel;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.game.rooms.types.tiles.RoomTileState;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;


public class RoomMapping {
    private RoomInstance room;

    private Tile[][] tiles;

    public RoomMapping(RoomInstance roomInstance) {
        this.room = roomInstance;
    }

    public void init() {
        if(this.getModel() == null) {
            return;
        }

        int sizeX = this.getModel().getSizeX();
        int sizeY = this.getModel().getSizeY();

        this.tiles = new Tile[sizeX][sizeY];

        for (int x = 0; x < sizeX; x++) {
            Tile[] xArray = new Tile[sizeY];

            for (int y = 0; y < sizeY; y++) {
                Tile instance = new Tile(this, new Position(x, y, 0d));
                instance.reload();

                xArray[y] = instance;
            }

            this.tiles[x] = xArray;
        }
    }

    public void tick() {
        // clear out the entity grid
        for(int x = 0; x < tiles.length; x++) {
            for(int y = 0; y < tiles[x].length; y++) {
                List<GenericEntity> entitiesToRemove = new ArrayList<>();

                try {
                    Tile tile = this.tiles[x][y];

                    for (GenericEntity entity : tile.getEntities()) {
                        if (entity instanceof PlayerEntity) {
                            if (((PlayerEntity) entity).getPlayer() == null) {
                                entitiesToRemove.add(entity);
                            }
                        }
                    }

                    for (GenericEntity entityToRemove : entitiesToRemove) {
                        tile.getEntities().remove(entityToRemove);
                    }
                } catch(Exception e) {
                    // TODO: Look into why this would cause an exception...
                }

                entitiesToRemove.clear();
            }
        }
    }

    public void updateTile(int x, int y) {
        if(x < 0 || y < 0) {
            return;
        }

        if (this.tiles.length > x) {
            if (tiles[x].length > y)
                this.tiles[x][y].reload();
        }
    }

    public Tile getTile(Position position) {
        if(position == null) return null;

        return this.getTile(position.getX(), position.getY());
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0) return null;
        if (x >= this.tiles.length || (this.tiles[x] == null || y >= this.tiles[x].length)) return null;

        return this.tiles[x][y];
    }

    public boolean positionHasUser(Position position) {
        return positionHasUser(null, position);
    }

    public boolean positionHasUser(Integer entityId, Position position) {
        boolean hasMountedPet = false;
        int entitySize = 0;
        boolean hasMe = false;

        if (entityId == null || entityId == -1)
            return false;

        for (GenericEntity entity : this.room.getEntities().getEntitiesAt(position)) {
            entitySize++;

            if (entity instanceof PetEntity && entity.hasMount()) {
                hasMountedPet = true;
            }

            // Do we need a null check here? Not sure yet..
            if (entityId != 0 && entity.getId() == entityId) {
                hasMe = true;
            }
        }

        return !(hasMe && entitySize == 1) && !hasMountedPet && entitySize > 0;
    }

    public boolean canStepUpwards(double height0, double height1) {
        return (height0 - height1) <= 1.5;
    }

    public boolean isValidEntityStep(GenericEntity entity, Position currentPosition, Position toPosition, boolean isFinalMove) {

        if (entity != null)
            return isValidStep(entity.getId(), currentPosition, toPosition, isFinalMove, false);
        else
            return isValidStep(0, currentPosition, toPosition, isFinalMove, true);
    }

    public boolean isValidStep(Position from, Position to, boolean lastStep) {
        return isValidStep(null, from, to, lastStep, false);
    }

    public boolean isValidStep(Position from, Position to, boolean lastStep, boolean isFloorItem) {
        return isValidStep(null, from, to, lastStep, isFloorItem);
    }

    public boolean isValidStep(Integer entity, Position from, Position to, boolean lastStep, boolean isFloorItem) {
        if (from.getX() == to.getX() && from.getY() == to.getY()) {
            return true;
        }

        if (!(to.getX() < this.getModel().getSquareState().length)) {
            return false;
        }

        if (!isValidPosition(to) || (this.getModel().getSquareState()[to.getX()][to.getY()] == RoomTileState.INVALID)) {
            return false;
        }

        final boolean isAtDoor = this.getModel().getDoorX() == from.getX() && this.getModel().getDoorY() == from.getY();

        int entityId;

        if (entity == null) {
            entityId = -1;
        } else if (isFloorItem) {
            entityId = 0;
        } else {
            entityId = entity;
        }

        final int rotation = Position.calculateRotation(from, to);

        if (rotation == 1 || rotation == 3 || rotation == 5 || rotation == 7) {
            // Get all tiles at passing corners
            Tile left = null;
            Tile right = null;

            switch (rotation) {
                case 1:
                    left = this.getTile(from.squareInFront(rotation + 1));
                    right = this.getTile(to.squareBehind(rotation + 1));
                    break;

                case 3:
                    left = this.getTile(to.squareBehind(rotation + 1));
                    right = this.getTile(to.squareBehind(rotation - 1));
                    break;

                case 5:
                    left = this.getTile(from.squareInFront(rotation - 1));
                    right = this.getTile(to.squareBehind(rotation - 1));
                    break;

                case 7:
                    left = this.getTile(to.squareBehind(0));
                    right = this.getTile(from.squareInFront(rotation - 1));
                    break;
            }

            if (left != null && right != null) {
                if (left.getMovementNode() != RoomEntityMovementNode.OPEN && right.getMovementNode() != RoomEntityMovementNode.OPEN)
                    return false;
            }
        }

        final boolean positionHasUser = positionHasUser(entityId, to);

        if (positionHasUser) {
            if ((!room.getData().getAllowWalkthrough() || isFloorItem) && !isAtDoor) {
                return false;

            } else if ((room.getData().getAllowWalkthrough()) && lastStep && !isAtDoor) {
                return false;
            }
        }

        Tile tile = tiles[to.getX()][to.getY()];

        if (tile == null) {
            return false;
        }

        if (tile.getMovementNode() == RoomEntityMovementNode.CLOSED || (tile.getMovementNode() == RoomEntityMovementNode.END_OF_ROUTE && !lastStep)) {
            return false;
        }

        final double fromHeight = this.getStepHeight(from);
        final double toHeight = this.getStepHeight(to);

        if (fromHeight < toHeight && (toHeight - fromHeight) > 1.0) return false;

        return true;
    }

    public double getStepHeight(Position position) {
        if (this.tiles.length <= position.getX() || this.tiles[position.getX()].length <= position.getY()) return 0.0;

        Tile instance = this.tiles[position.getX()][position.getY()];

        if (!isValidPosition(instance.getPosition())) {
            return 0.0;
        }

        RoomTileStatusType tileStatus = instance.getStatus();
        double height = instance.getWalkHeight();

        if (tileStatus == null) {
            return 0.0;
        }

        return height;
    }

    public List<Position> tilesWithFurniture() {
        List<Position> tilesWithFurniture = Lists.newArrayList();

        for (int x = 0; x < this.tiles.length; x++) {
            for (int y = 0; y < this.tiles[x].length; y++) {
                if (this.tiles[x][y].hasItems()) tilesWithFurniture.add(new Position(x, y));
            }
        }

        return tilesWithFurniture;
    }

    public boolean isValidPosition(Position position) {
        return ((position.getX() >= 0) && (position.getY() >= 0) && (position.getX() < this.getModel().getSizeX()) && (position.getY() < this.getModel().getSizeY()));
    }

    public final RoomInstance getRoom() {
        return this.room;
    }

    public RoomModel getModel() {
        return this.room.getModel();
    }

    @Override
    public String toString() {
        String mapString = "";

        for (int y = 0; y < this.tiles.length; y++) {
            for (int x = 0; x < this.tiles[y].length; x++) {
                if (this.tiles[y][x].getMovementNode() == RoomEntityMovementNode.CLOSED) {
                    mapString += " ";
                } else {
                    mapString += "X";
                }
            }

            mapString += "\n";
        }

        return mapString;
    }

    public String visualiseEntityGrid() {
        final StringBuilder builder = new StringBuilder();

        for (int y = 0; y < this.tiles.length; y++) {
            for (int x = 0; x < this.tiles[y].length; x++) {
                if (this.tiles[y][x].getEntities().size() != 0) {
                    builder.append("E");
                } else {
                    builder.append("[]");
                }
            }

            builder.append("\n");
        }

        return builder.toString();
    }
}