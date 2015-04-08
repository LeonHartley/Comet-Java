package com.cometproject.server.game.rooms.types.mapping;

import com.cometproject.server.game.rooms.objects.RoomObject;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Pathfinder;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.AdjustableHeightFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.BedFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.GateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.MagicStackFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.snowboarding.SnowboardJumpFloorItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.tiles.RoomTileState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Tile {
    private RoomMapping mappingInstance;
    private Position position;

    private RoomEntityMovementNode movementNode;
    private RoomTileStatusType status;

    private boolean canStack;

    private int topItem = 0;
    private double stackHeight = 0d;

    private int originalTopItem = 0;
    private double originalHeight = 0d;

    private Position redirect = null;

    private boolean canPlaceItemHere = false;
    private boolean hasItems = false;
    private boolean hasMagicTile = false;

    private List<RoomItemFloor> items;
    public Set<GenericEntity> entities;

    public Tile(RoomMapping mappingInstance, Position position) {
        this.mappingInstance = mappingInstance;
        this.position = position;
        this.entities = new HashSet<>();
        this.items = new ArrayList<>(); // maybe change this in the future..

        this.reload();
    }

    public void reload() {
        // reset the tile data
        this.hasItems = false;
        this.redirect = null;
        this.movementNode = RoomEntityMovementNode.OPEN;
        this.status = RoomTileStatusType.NONE;
        this.canStack = true;
        this.hasMagicTile = false;
        this.topItem = 0;
        this.originalHeight = 0d;
        this.originalTopItem = 0;
        this.stackHeight = 0d;

        if (this.mappingInstance.getModel().getSquareState()[this.getPosition().getX()][this.getPosition().getY()] == null) {
            this.canPlaceItemHere = false;
        } else {
            this.canPlaceItemHere = this.mappingInstance.getModel().getSquareState()[this.getPosition().getX()][this.getPosition().getY()].equals(RoomTileState.VALID);
        }

        // component item is an item that can be used along with an item that overrides the height.
        boolean hasComponentItem = false;

        double highestHeight = 0d;
        int highestItem = 0;

        Double staticOverrideHeight = null;
        Double overrideHeight = null;

        this.items.clear();

        for (RoomItemFloor item : mappingInstance.getRoom().getItems().getFloorItems()) {
            if (item == null || item.getDefinition() == null) continue; // it's null!

            if (item.getPosition().getX() == this.position.getX() && item.getPosition().getY() == this.position.getY()) {
                items.add(item);
            } else {
                List<AffectedTile> affectedTiles = AffectedTile.getAffectedTilesAt(
                        item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getPosition().getX(), item.getPosition().getY(), item.getRotation());

                for (AffectedTile tile : affectedTiles) {
                    if (this.position.getX() == tile.x && this.position.getY() == tile.y) {
                        if (!items.contains(item)) {
                            items.add(item);
                        }
                    }
                }
            }
        }

        for (RoomItemFloor item : items) {
            if (item.getDefinition() == null)
                continue;

            System.out.println(item.getId());

            this.hasItems = true;

            final double totalHeight = item.getPosition().getZ() + (item.getOverrideHeight() != -1d ? item.getOverrideHeight() : item.getDefinition().getHeight());

            if (totalHeight > highestHeight) {
                highestHeight = totalHeight;
                highestItem = item.getId();
            }

            final boolean isGate = item instanceof GateFloorItem;

            if(item instanceof MagicStackFloorItem) {
                this.hasMagicTile = true;
            }

            if (!item.getDefinition().canWalk() && !isGate) {
                if(highestItem == item.getId())
                    movementNode = RoomEntityMovementNode.CLOSED;
            }

            switch (item.getDefinition().getInteraction().toLowerCase()) {
                case "bed":
                    status = RoomTileStatusType.LAY;
                    movementNode = RoomEntityMovementNode.END_OF_ROUTE;

                    if (item.getRotation() == 2 || item.getRotation() == 6) {
                        this.redirect = new Position(item.getPosition().getX(), this.getPosition().getY());
                    } else if (item.getRotation() == 0 || item.getRotation() == 4) {
                        this.redirect = new Position(this.getPosition().getX(), item.getPosition().getY());
                    }

                    break;

                case "gate":
                    movementNode = ((GateFloorItem) item).isOpen() ? RoomEntityMovementNode.OPEN : RoomEntityMovementNode.CLOSED;
                    break;

                case "onewaygate":
                    movementNode = RoomEntityMovementNode.CLOSED;
                    break;

                case "wf_pyramid":
                    movementNode = item.getExtraData().equals("1") ? RoomEntityMovementNode.OPEN : RoomEntityMovementNode.CLOSED;
                    break;
            }

            if (item instanceof SnowboardJumpFloorItem) {
                hasComponentItem = true;
            }

            if (item.getDefinition().canSit()) {
                status = RoomTileStatusType.SIT;
                movementNode = RoomEntityMovementNode.END_OF_ROUTE;
            }

            if (item.getDefinition().getInteraction().equals("bed")) {
                status = RoomTileStatusType.LAY;
                movementNode = RoomEntityMovementNode.END_OF_ROUTE;
            }

            if (!item.getDefinition().canStack()) {
                this.canStack = false;
            }

            if (item.getOverrideHeight() != -1d) {
                if(item instanceof MagicStackFloorItem) {
                    staticOverrideHeight = item.getOverrideHeight();
                }

                if(overrideHeight != null) {
                    overrideHeight += item.getOverrideHeight() + (hasComponentItem ? 1.0 : 0d);
                } else {
                    overrideHeight = item.getOverrideHeight() + (hasComponentItem ? 1.0 : 0d);
                }
            }
        }

        if (overrideHeight != null) {
            this.canStack = true;
            this.stackHeight = staticOverrideHeight != null ? staticOverrideHeight : overrideHeight;

            this.originalHeight = highestHeight;
        } else {
            this.stackHeight = highestHeight;
        }

        this.topItem = highestItem;

        if (this.stackHeight == 0d) {
            this.stackHeight = this.mappingInstance.getModel().getSquareHeight()[this.position.getX()][this.position.getY()];
        }

        if(this.originalHeight == 0)
            this.originalHeight = this.stackHeight;
    }

    public RoomEntityMovementNode getMovementNode() {
        return this.movementNode;
    }

    public double getStackHeight() {
        return this.getStackHeight(null);
    }

    public double getStackHeight(RoomItemFloor itemToStack) {
        RoomItemFloor topItem = this.getTopItemInstance();

        double stackHeight;

        if(this.hasMagicTile() || (topItem != null && topItem instanceof AdjustableHeightFloorItem)) {
            stackHeight = this.stackHeight;
        } else {
            stackHeight = itemToStack != null && itemToStack.getId() == this.getTopItem() ? itemToStack.getPosition().getZ() : this.originalHeight;
        }

        return stackHeight;
    }

    public double getWalkHeight() {
        double height = this.stackHeight;

        RoomItemFloor roomItemFloor = this.mappingInstance.getRoom().getItems().getFloorItem(this.topItem);

        if (roomItemFloor != null && (roomItemFloor.getDefinition().canSit() || roomItemFloor instanceof BedFloorItem || roomItemFloor instanceof SnowboardJumpFloorItem)) {
            if (roomItemFloor instanceof SnowboardJumpFloorItem) {
                height += 1.0;
            } else {
                height -= roomItemFloor.getDefinition().getHeight();
            }
        }

        return height;
    }

    public boolean isReachable(RoomObject object) {
        List<Square> path = Pathfinder.getInstance().makePath(object, this.position);
        return path != null && path.size() > 0;
    }

    public GenericEntity getEntity() {
        for(GenericEntity entity : this.getEntities()) {
            return entity;
        }

        return null;
    }

    public Set<GenericEntity> getEntities() {
        return this.entities;
    }

    public RoomTileStatusType getStatus() {
        return this.status;
    }

    public Position getPosition() {
        return this.position;
    }

    public boolean canStack() {
        return this.canStack;
    }

    public int getTopItem() {
        return this.topItem;
    }

    public RoomItemFloor getTopItemInstance() {
        return this.mappingInstance.getRoom().getItems().getFloorItem(this.getTopItem());
    }

    public void setTopItem(int topItem) {
        this.topItem = topItem;
    }

    public Position getRedirect() {
        return redirect;
    }

    public void setRedirect(Position redirect) {
        this.redirect = redirect;
    }

    public int getOriginalTopItem() {
        return originalTopItem;
    }

    public double getOriginalHeight() {
        return originalHeight;
    }

    public boolean canPlaceItemHere() {
        return canPlaceItemHere;
    }

    public boolean hasItems() {
        return hasItems;
    }

    public double getTileHeight() {
        return this.mappingInstance.getModel().getSquareHeight()[this.position.getX()][this.position.getY()];
    }

    public boolean hasMagicTile() {
        return this.hasMagicTile;
    }

    public List<RoomItemFloor> getItems() {
        return items;
    }
}
