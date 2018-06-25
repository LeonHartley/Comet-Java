package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai.BanzaiPuckFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomEntityMovementNode;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.api.game.rooms.models.RoomTileState;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.utilities.Direction;
import com.cometproject.storage.api.StorageContext;

import java.util.HashMap;
import java.util.Map;


public abstract class RollableFloorItem extends RoomItemFloor {
    public static final int KICK_POWER = 6;

    private boolean isRolling = false;
    private RoomEntity kickerEntity;
    private boolean skipNext = false;
    private boolean wasDribbling = false;
    private int rollStage = -1;

    private boolean skip = false;

    private int animationMode = -1;

    public RollableFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    public static void roll(RoomItemFloor item, Position from, Position to, Room room) {
        final RollableFloorItem rollableFloorItem = (RollableFloorItem) item;

        final Map<Integer, Double> items = new HashMap<>();

        items.put(item.getVirtualId(), item.getPosition().getZ());
        room.getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(from.copy(), to.copy(), item.getVirtualId(), 0, items));
//        item.sendUpdate();

        /*
[1503332665217] Incoming: [1092]: [0][0][0][11][4]D[2][1][0][0][2]@[0]
[1503332665216] Incoming: [3486]: [0][0][0];[13]???|[0][0][13]?[0][0][0][2][0][0][0][0][0][0][3][0][3]0.0[0][6]1.0E-6[0][0][0][0][0][0][0][0][0][2]11????[0][0][0][0][1]???

[1503332665048] Incoming: [1092]: [0][0][0][11][4]D[2][2][0][0][1]@[0]
[1503332665047] Incoming: [3486]: [0][0][0];[13]???|[0][0][13]?[0][0][0][1][0][0][0][0][0][0][5][0][3]0.0[0][6]1.0E-6[0][0][0][0][0][0][0][0][0][2]33????[0][0][0][0][1]???
delay: 126ms

[1503332664922] Incoming: [1092]: [0][0][0][11][4]D[2][3][0][0][2]@[0]
[1503332664921] Incoming: [3486]: [0][0][0];[13]???|[0][0][13]?[0][0][0][2][0][0][0][0][0][0][5][0][3]0.0[0][6]1.0E-6[0][0][0][0][0][0][0][0][0][2]44????[0][0][0][0][1]???

delay: 169ms
[1503332664821] Incoming: [1092]: [0][0][0][11][4]D[2][4][0][0][3]@[0]
[1503332664821] [b683bf485154731dea9fc99eab215556]
[1503332664821] Incoming: [3486]: [0][0][0];[13]???|[0][0][13]?[0][0][0][3][0][0][0][0][0][0][5][0][3]0.0[0][6]1.0E-6[0][0][0][0][0][0][0][0][0][2]55????[0][0][0][0][1]???
[1503332664771] [28e1d8306d60eba9566ddb95f64f5a36]
[1503332664771] Incoming: [3731]: [0][0][0]=?[0][0][0][1][0][0][0][0][0][0][0][5][0][0][0][0][3]0.0[0][0][0][5][0][0][0][5][0]/flatctrl 4/mv 4,19,1.0E-6//
[1503332664356] [e759ffbfd8bdfc8ae7721b1e33c9d49b]*/
    }

    public static Position calculatePosition(int x, int y, int playerRotation) {
        return Position.calculatePosition(x, y, playerRotation, false, 1);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (this.skipNext && (this.kickerEntity != null && entity.getId() == this.kickerEntity.getId())) {
            this.kickerEntity = null;
            this.skipNext = false;
            return;
        }

        if (entity instanceof PlayerEntity && this instanceof BanzaiPuckFloorItem) {
            this.getItemData().setData((((PlayerEntity) entity).getGameTeam().getTeamId() + 1) + "");
            this.sendUpdate();
        }

        boolean isOnBall = entity.getWalkingGoal().getX() == this.getPosition().getX() && entity.getWalkingGoal().getY() == this.getPosition().getY();

        this.setRotation(entity.getBodyRotation());

        if (isOnBall && !this.wasDribbling) {
            if (entity instanceof PlayerEntity) {
                this.kickerEntity = entity;
            }

            this.wasDribbling = false;
            this.rollStage = 0;
            this.animationMode = 6;
            this.rollBall(entity.getPosition(), entity.getBodyRotation());
        } else if (isOnBall) {
            if (entity.getPreviousSteps() != 2) {
                this.rollSingle(entity);
            }

            this.wasDribbling = false;
        } else {
            this.rollSingle(entity);
            this.wasDribbling = true;
        }
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (!this.skipNext) {
            this.rollBall(this.getPosition(), Direction.get(entity.getBodyRotation()).invert().num);
        } else {
            this.skipNext = false;
        }
    }

    private void rollBall(Position from, int rotation) {
        if (!DistanceCalculator.tilesTouching(this.getPosition().getX(), this.getPosition().getY(), from.getX(), from.getY())) {
            return;
        }

        this.setRotation(rotation);
        this.isRolling = true;
        this.rollStage = 0;

        this.onTickComplete();
    }

    @Override
    public void onTickComplete() {
        if (!this.isRolling || this.rollStage == -1 || this.rollStage >= KICK_POWER) {
            this.isRolling = false;
            this.rollStage = -1;
            this.skipNext = false;
            this.wasDribbling = false;
            return;
        }

        this.rollStage++;

        boolean isStart = this.rollStage == 1;
        boolean isLast = this.rollStage >= KICK_POWER;
//
//        int maxSteps = isStart ? 3 : 2;
//        int stepsTaken = 1;
//
//        Position position = this.getNextPosition();
//
//        for (int i = this.rollStage; i < KICK_POWER; i++) {
//
//
//        }

        if (isStart) {
            // the first roll... let's do some magic.

            int tiles = 1;
            Position position = this.getNextPosition();

            if (!this.isValidRoll(position)) {
                position = this.getNextPosition(position.getFlag(), position.squareBehind(position.getFlag()));
            }

            int count = isStart ? 2 : 2;

            // can we skip some tiles?
            for (int i = 0; i < count && (this.rollStage + i < KICK_POWER); i++) {
                Position nextPosition = this.getNextPosition(position.getFlag(), position.squareInFront(position.getFlag()));

                if (!this.isValidRoll(nextPosition)) {
                    break;
                }

                if (nextPosition.getFlag() != this.getRotation() || (this.rollStage + i) > KICK_POWER) {
                    // we hit a snag
                    break;
                }

                tiles = i;
                position = nextPosition;
            }

            if (position.getFlag() == -1) {
                position.setFlag(kickerEntity.getBodyRotation());
            }

            double distanceMoved = position.distanceTo(this.getPosition());
            this.rollStage += distanceMoved;

            this.getItemData().setData("55");
            this.sendUpdate();

            this.moveTo(position, position.getFlag());
            //System.out.println(tiles);
            this.setTicks(RoomItemFactory.getProcessTime(tiles * 0.5));
        } else {
            Position nextPosition = this.getNextPosition();
            Position newPosition;

            if (this.isValidRoll(nextPosition)) {
                newPosition = nextPosition;
            } else {
                newPosition = this.getNextPosition();
            }

            if (!this.isValidRoll(newPosition)) {
                return;
            }

            if (newPosition.getFlag() == -1) {
                newPosition.setFlag(kickerEntity.getBodyRotation());
            }

            this.getItemData().setData((KICK_POWER - (this.rollStage - 1) == 0 ? 3 : (KICK_POWER - (this.rollStage - 1)) * 11));
            this.sendUpdate();

            this.moveTo(newPosition, newPosition.getFlag());
            this.setTicks(RoomItemFactory.getProcessTime(this.getDelay(this.rollStage)));
        }
    }

    private boolean isValidRoll(int x, int y) {
        return false;
    }

    private boolean isValidRoll(Position position) {
        RoomTile tile = this.getRoom().getMapping().getTile(position.getX(), position.getY());

        if (tile != null) {
            if (tile.canPlaceItemHere() && tile.getMovementNode() == RoomEntityMovementNode.OPEN && tile.getState() == RoomTileState.VALID) {
                if (tile.getEntities().size() != 0) {
                    return false;
                }

                return true;
            }
        }

        return false;
    }

    public Position getNextPosition() {
        return this.getNextPosition(this.getRotation(), this.getPosition().squareInFront(this.getRotation()));
    }

    private Position getNextPosition(int rotation, Position position) {
        if (!this.isValidRoll(position)) {
            rotation = Position.getInvertedRotation(rotation);
            position = this.getPosition().squareInFront(rotation);

            if (!this.isValidRoll(position)) {
                // reset the position back the original
                position = this.getPosition();

                switch (rotation) {
                    case Position.NORTH:
                        rotation = Position.SOUTH;
                        break;

                    case Position.NORTH_EAST:
                        rotation = Position.SOUTH_EAST;

                        if (!this.isValidRoll(position.squareInFront(rotation))) {
                            rotation = Position.SOUTH_WEST;
                        }

                        break;

                    case Position.EAST:
                        rotation = Position.WEST;
                        break;

                    case Position.SOUTH_EAST:
                        rotation = Position.SOUTH_WEST;

                        if (!this.isValidRoll(position.squareInFront(rotation))) {
                            rotation = Position.NORTH_EAST;

                            if (!this.isValidRoll(position.squareInFront(rotation))) {
                                rotation = Position.NORTH_EAST;
                            }
                        }
                        break;

                    case Position.SOUTH:
                        rotation = Position.NORTH;
                        break;

                    case Position.SOUTH_WEST:
                        rotation = Position.NORTH_WEST;

                        if (!this.isValidRoll(position.squareInFront(rotation))) {
                            rotation = Position.SOUTH_EAST;
                        }
                        break;

                    case Position.WEST:
                        rotation = Position.EAST;
                        break;

                    case Position.NORTH_WEST:
                        rotation = Position.SOUTH_WEST;

                        if (!this.isValidRoll(position.squareInFront(rotation))) {
                            rotation = Position.SOUTH_EAST;
                        }
                        break;
                }

                position = position.squareInFront(rotation);
            }
        }

        position.setFlag(rotation);
        return position;
    }

    private void rollSingle(RoomEntity entity) {
        if (this.isRolling || !entity.getPosition().touching(this.getPosition())) {
            return;
        }

        if (entity instanceof PlayerEntity) {
            this.kickerEntity = entity;

//            if (kickerEntity.getBodyRotation() % 2 != 0) {
//                return false;
//            }
        }

        this.isRolling = true;

        Position newPosition;

        if (this.isValidRoll(this.getNextPosition())) {
            newPosition = calculatePosition(this.getPosition().getX(), this.getPosition().getY(), entity.getBodyRotation());
        } else {
            newPosition = Position.calculatePosition(this.getPosition().getX(), this.getPosition().getY(), entity.getBodyRotation(), true, 1);
            this.setRotation(Direction.get(this.getRotation()).invert().num);
        }

        if (!this.isValidRoll(newPosition)) {
            return;
        }

        this.getItemData().setData("11");
        this.moveTo(newPosition, entity.getBodyRotation());
        this.isRolling = false;
        this.wasDribbling = false;
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTriggered) {
        if (isWiredTriggered) return false;

        if (entity instanceof PlayerEntity) {
            this.kickerEntity = entity;
        }

        this.skipNext = true;
        this.rollSingle(entity);
        return true;
    }

    @Override
    public void onPositionChanged(Position newPosition) {
        this.isRolling = false;
        this.kickerEntity = null;
        this.skipNext = false;
        this.rollStage = -1;
        this.wasDribbling = false;
    }

    private void moveTo(Position pos, int rotation) {
        RoomTile newTile = this.getRoom().getMapping().getTile(pos);

        if (newTile == null) {
            return;
        }

        pos.setZ(newTile.getStackHeight());

        roll(this, this.getPosition().copy(), pos.copy(), this.getRoom());

        RoomTile tile = this.getRoom().getMapping().getTile(this.getPosition());

        this.setRotation(rotation);

        this.getPosition().setX(pos.getX());
        this.getPosition().setY(pos.getY());

        if (tile != null) {
            tile.reload();
        }

        newTile.reload();

        // tell all other items on the new square that there's a new item. (good method of updating score...)
        for (RoomItemFloor floorItem : this.getRoom().getItems().getItemsOnSquare(pos.getX(), pos.getY())) {
            floorItem.onItemAddedToStack(this);
        }

        this.getPosition().setZ(pos.getZ());
        this.getRoom().getItemProcess().saveItem(this);
    }

    private double getDelay(int i) {
        if (i == 5) {
            return 0.5;
        } else if (i == 6) {
            return 1.0;
        }

//        System.out.println(i);
        return 0.5;
    }

    public RoomEntity getPusher() {
        return kickerEntity;
    }
}