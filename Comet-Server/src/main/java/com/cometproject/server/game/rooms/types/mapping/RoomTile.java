package com.cometproject.server.game.rooms.types.mapping;

import com.cometproject.server.game.rooms.objects.RoomObject;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.types.ItemPathfinder;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.snowboarding.SnowboardJumpFloorItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.tiles.RoomTileState;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class RoomTile {
    private RoomMapping mappingInstance;
    private Position position;

    private RoomEntityMovementNode movementNode;
    private RoomTileStatusType status;
    private RoomTileState state;

    private boolean canStack;

    private long topItem = 0;
    private double stackHeight = 0d;

    private long originalTopItem = 0;
    private double originalHeight = 0d;

    private Position redirect = null;

    private boolean canPlaceItemHere = false;
    private boolean hasItems = false;
    private boolean hasMagicTile = false;

    private boolean hasAdjustableHeight = false;

    private List<RoomItemFloor> items;
    public Set<RoomEntity> entities;

    private Map<Integer, Consumer<RoomEntity>> pendingEvents = new ConcurrentHashMap<>();

    public RoomTile(RoomMapping mappingInstance, Position position) {
        this.mappingInstance = mappingInstance;
        this.position = position;
        this.entities = new ConcurrentHashSet<>();
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
        this.hasAdjustableHeight = false;

        if (this.mappingInstance.getModel().getSquareState()[this.getPosition().getX()][this.getPosition().getY()] == null) {
            this.canPlaceItemHere = false;
            this.state = RoomTileState.INVALID;
        } else {
            this.canPlaceItemHere = this.mappingInstance.getModel().getSquareState()[this.getPosition().getX()][this.getPosition().getY()].equals(RoomTileState.VALID);
            this.state = this.mappingInstance.getModel().getSquareState()[this.getPosition().getX()][this.getPosition().getY()];
        }

        // component item is an item that can be used along with an item that overrides the height.
        boolean hasComponentItem = false;

        double highestHeight = 0d;
        long highestItem = 0;

        Double staticOverrideHeight = null;
        Double overrideHeight = null;

        this.items.clear();

        for (Map.Entry<Long, RoomItemFloor> itemEntry : mappingInstance.getRoom().getItems().getFloorItems().entrySet()) {
            final RoomItemFloor item = itemEntry.getValue();

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

        for (RoomItemFloor item : new ArrayList<>(items)) {
            if (item.getDefinition() == null)
                continue;

            this.hasItems = true;

            final double totalHeight = item.getPosition().getZ() + (item.getOverrideHeight() != -1d ? item.getOverrideHeight() : item.getDefinition().getHeight());

            if (totalHeight > highestHeight) {
                highestHeight = totalHeight;
                highestItem = item.getId();
            }

            final boolean isGate = item instanceof GateFloorItem;

            if (item instanceof MagicStackFloorItem) {
                this.hasMagicTile = true;
            }

            if (!item.getDefinition().canWalk() && !isGate) {
                if (highestItem == item.getId())
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
                if (item instanceof MagicStackFloorItem) {
                    staticOverrideHeight = item.getOverrideHeight();
                }

                if (overrideHeight != null) {
                    overrideHeight += item.getOverrideHeight() + (hasComponentItem ? 1.0 : 0d);
                } else {
                    overrideHeight = item.getOverrideHeight() + (hasComponentItem ? 1.0 : 0d);
                }
            }
        }

        if (overrideHeight != null) {
            this.hasAdjustableHeight = true;
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

        if (this.originalHeight == 0)
            this.originalHeight = this.stackHeight;
    }

    public void dispose() {
        this.pendingEvents.clear();
        this.items.clear();
        this.entities.clear();
    }

    public void onEntityEntersTile(RoomEntity entity) {
        if (this.pendingEvents.containsKey(entity.getId())) {
            this.pendingEvents.get(entity.getId()).accept(entity);
            this.pendingEvents.remove(entity.getId());
        }
    }

    public void scheduleEvent(int entityId, Consumer<RoomEntity> event) {
        this.pendingEvents.put(entityId, event);
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

        if (this.hasMagicTile() || (topItem != null && topItem instanceof AdjustableHeightFloorItem)) {
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

        if (this.hasAdjustableHeight && roomItemFloor instanceof SeatFloorItem) {
            height += ((SeatFloorItem) roomItemFloor).getSitHeight();
        }

        return height;
    }

    public boolean isReachable(RoomObject object) {
        List<Square> path = ItemPathfinder.getInstance().makePath(object, this.position);
        return path != null && path.size() > 0;
    }

    public RoomEntity getEntity() {
        for (RoomEntity entity : this.getEntities()) {
            return entity;
        }

        return null;
    }

    public Set<RoomEntity> getEntities() {
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

    public long getTopItem() {
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

    public long getOriginalTopItem() {
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

    public boolean hasAdjustableHeight() {
        return hasAdjustableHeight;
    }

    public RoomTileState getState() {
        return state;
    }
}
