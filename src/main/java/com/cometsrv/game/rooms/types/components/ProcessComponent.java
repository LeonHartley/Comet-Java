package com.cometsrv.game.rooms.types.components;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.items.interactions.InteractionAction;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.avatars.pathfinding.Square;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.entities.RoomEntityType;
import com.cometsrv.game.rooms.entities.types.BotEntity;
import com.cometsrv.game.rooms.entities.types.PetEntity;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometsrv.tasks.CometTask;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ProcessComponent implements CometTask {
    private final int ROOM_DISPOSE_TIME = Integer.parseInt(Comet.getServer().getConfig().get("comet.room.dispose.cycles"));
    private int disposeCycles = 0;

    private FastList<GenericEntity> usersToUpdate;

    private Room room;

    private Logger log;
    private ScheduledFuture myFuture;
    private boolean active = false;

    public ProcessComponent(Room room) {
        this.room = room;
        this.log = Logger.getLogger("Room Process [" + room.getData().getName() + "]");
    }

    public void start() {
        if(this.active) {
            stop();
        }

        this.active = true;
        this.myFuture = Comet.getServer().getThreadManagement().executePeriodic(this, 500, 500, TimeUnit.MILLISECONDS);

        log.debug("Processing started");
    }

    public void stop() {
        if(this.myFuture != null) {
            this.active = false;
            this.myFuture.cancel(false);

            log.debug("Processing stopped");
        }
    }

    @Override
    public void run() {
        if (!this.active) {
            return;
        }

        // Dispose the room if it has been idle for a certain amount of time
        if (this.getRoom().getEntities().count() == 0) {
            if (this.disposeCycles >= ROOM_DISPOSE_TIME) {
                this.getRoom().dispose();
                return;
            }

            this.disposeCycles++;
        } else {
            if (this.disposeCycles >= 0) {
                this.disposeCycles = 0;
            }
        }

        long timeStart = System.currentTimeMillis();

        // Reset the users to update
        if (this.usersToUpdate == null) {
            this.usersToUpdate = new FastList<>();
        } else if (this.usersToUpdate.size() > 0) {
            this.usersToUpdate.clear();
        }

        Map<Integer, GenericEntity> entities = this.room.getEntities().getEntitiesCollection();

        List<GenericEntity>[][] entityGrid = new ArrayList[this.getRoom().getModel().getSizeX()][this.getRoom().getModel().getSizeY()];

        for (GenericEntity entity : entities.values()) {
            // Process each entity as its own
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                processPlayerEntity((PlayerEntity) entity);
            } else if (entity.getEntityType() == RoomEntityType.BOT) {
                processBotEntity((BotEntity) entity);
            } else if (entity.getEntityType() == RoomEntityType.PET) {
                processPetEntity((PetEntity) entity);
            }

            // Create the new entity grid
            if (entityGrid[entity.getPosition().getX()][entity.getPosition().getY()] == null) {
                entityGrid[entity.getPosition().getX()][entity.getPosition().getY()] = new ArrayList<GenericEntity>();
            }

            entityGrid[entity.getPosition().getX()][entity.getPosition().getY()].add(entity);

            // Process anything generic for all entities below this line

            // Handle signs
            if (entity.hasStatus("sign") && !entity.isDisplayingSign()) {
                entity.removeStatus("sign");
                entity.markNeedsUpdate();
            }

            if (entity.needsUpdate()) {
                entity.markNeedsUpdateComplete();

                this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entity));
            }
        }

        // Update the entity grid
        this.getRoom().getEntities().replaceEntityGrid(entityGrid);
    }

    protected void processPlayerEntity(PlayerEntity entity) {
        // Cleanup if the entity is offline
        if (entity.getPlayer() == null || entity.getRoom() == null) {
            this.room.getEntities().removeEntity(entity);
            this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entity));
        }

        if (entity.getPlayer().floodTime >= 0.5) {
            entity.getPlayer().floodTime -= 0.5;

            if (entity.getPlayer().floodTime < 0) {
                entity.getPlayer().floodTime = 0;
            }
        }

        if (entity.hasStatus("mv")) {
            entity.removeStatus("mv");
            entity.markNeedsUpdate();
        }

        // Check if we are wanting to walk to a location
        if (entity.getWalkingPath() != null) {
            entity.setProcessingPath(new ArrayList<Square>(entity.getWalkingPath()));

            // Clear the walking path now we have a goal set
            entity.getWalkingPath().clear();
            entity.setWalkingPath(null);
        }

        // We should store players which need removing from the room here and process it last
        List<PlayerEntity> leavingRoom = new ArrayList<>();

        // Do we have a position to set from previous moves?
        if (entity.getPositionToSet() != null) {
            if ((entity.getPositionToSet().getX() == this.room.getModel().getDoorX()) && (entity.getPositionToSet().getY() == this.room.getModel().getDoorY())) {
                leavingRoom.add(entity);
                return;
            }

            // Copy the current position before updating
            Position3D currentPosition = new Position3D(entity.getPosition());

            for(FloorItem item : this.getRoom().getItems().getItemsOnSquare(currentPosition.getX(), currentPosition.getY())) {
                item.setNeedsUpdate(true, InteractionAction.ON_WALK, entity, 0);
            }

            // Create the new position
            Position3D newPosition = new Position3D();
            newPosition.setX(entity.getPositionToSet().getX());
            newPosition.setY(entity.getPositionToSet().getY());
            newPosition.setZ(entity.getPositionToSet().getZ());

            // Calculate highest seat point
            double seatHeight = this.getRoom().getModel().getSquareHeight()[entity.getPositionToSet().getX()][entity.getPositionToSet().getY()];

            for(FloorItem item : this.getRoom().getItems().getItemsOnSquare(entity.getPositionToSet().getX(), entity.getPositionToSet().getY())) {
                if (item.getDefinition().canSit) {
                    seatHeight += item.getHeight();
                }
            }

            List<FloorItem> itemsOnSq = this.getRoom().getItems().getItemsOnSquare(entity.getPositionToSet().getX(), entity.getPositionToSet().getY());

            // Apply sit
            for(FloorItem item : itemsOnSq) {
                item.setNeedsUpdate(true, InteractionAction.ON_WALK, entity, 1);

                if (item.getDefinition().canSit) {
                    double height = item.getHeight();

                    if (height < 1.0) {
                        height = 1.0;
                    } else if (itemsOnSq.size() == 1 && height > 1.0) {
                        height = 1.0;
                    }

                    entity.setBodyRotation(item.getRotation());
                    entity.setHeadRotation(item.getRotation());
                    entity.addStatus("sit", String.valueOf(height).replace(',', '.'));
                    entity.markNeedsUpdate();
                }
            }

            entity.setPosition(newPosition);

            // We can also handle walk to + interact here in the future!
        }

        if (entity.isWalking()) {
            Square nextSq = entity.getProcessingPath().get(0);
            entity.getProcessingPath().remove(nextSq);

            boolean isLastStep = (entity.getProcessingPath().size() == 0);

            if (nextSq != null && entity.getRoom().getMapping().isValidStep(entity.getPosition(), new Position3D(nextSq.x, nextSq.y, 0), isLastStep)) {
                Position3D currentPos = entity.getPosition() != null ? entity.getPosition() : new Position3D(0, 0, 0);
                entity.setBodyRotation(Position3D.calculateRotation(currentPos.getX(), currentPos.getY(), nextSq.x, nextSq.y, false));
                entity.setHeadRotation(entity.getBodyRotation());

                double height = this.getRoom().getModel().getSquareHeight()[nextSq.x][nextSq.y];

                for(FloorItem item : this.getRoom().getItems().getItemsOnSquare(nextSq.x, nextSq.y)) {
                    height += item.getHeight();
                }

                entity.addStatus("mv", String.valueOf(nextSq.x).concat(",").concat(String.valueOf(nextSq.y)).concat(",").concat(String.valueOf(height)));

                if (entity.hasStatus("sit")) {
                    entity.removeStatus("sit");
                }

                if (entity.hasStatus("lay")) {
                    entity.removeStatus("lay");
                }

                entity.updateAndSetPosition(new Position3D(nextSq.x, nextSq.y, height));
                entity.markNeedsUpdate();
            }
        }

        // Remove entities from room
        for (PlayerEntity entity0 : leavingRoom) {
            entity0.leaveRoom(false, false, true);
        }
    }

    protected void processBotEntity(BotEntity entity) {

    }

    protected void processPetEntity(PetEntity entity) {

    }

    private void removeFromRoom(GenericEntity entity) {
        this.room.getEntities().removeEntity(entity);
        this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entity));
    }

    public void dispose() {
        this.active = false;
        this.myFuture.cancel(false);
    }

    public boolean isActive() {
        return this.active;
    }

    public Room getRoom() { return this.room; }
}
