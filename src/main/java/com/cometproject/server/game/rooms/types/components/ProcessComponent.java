package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.items.interactions.InteractionAction;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.Square;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.RoomEntityType;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.types.TriggerType;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.ShoutMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.utilities.RandomInteger;
import com.cometproject.server.utilities.TimeSpan;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
        if (this.active) {
            stop();
        }

        this.active = true;
        this.myFuture = Comet.getServer().getThreadManagement().executePeriodic(this, 500, 500, TimeUnit.MILLISECONDS);

        log.debug("Processing started");
    }

    public void stop() {
        if (this.myFuture != null) {
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

        try {

            // Dispose the room if it has been idle for a certain amount of time
            if (this.getRoom().getEntities().playerCount() == 0) {
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
                    PlayerEntity playerEntity = (PlayerEntity) entity;

                    if (playerEntity.getPlayer() == null) {
                        removeFromRoom(playerEntity);
                        continue;
                    }

                    processPlayerEntity(playerEntity);
                } else if (entity.getEntityType() == RoomEntityType.BOT) {
                    processBotEntity((BotEntity) entity);
                } else if (entity.getEntityType() == RoomEntityType.PET) {
                    processPetEntity((PetEntity) entity);
                }

                // TODO: Change the movement handling to do it per entity, not per entity type. (Remove it from the process{EntityType}Entity method and create a new one)

                // Create the new entity grid
                if (entityGrid[entity.getPosition().getX()][entity.getPosition().getY()] == null) {
                    entityGrid[entity.getPosition().getX()][entity.getPosition().getY()] = new ArrayList<GenericEntity>();
                }

                entityGrid[entity.getPosition().getX()][entity.getPosition().getY()].add(entity);

                // Process anything generic for all entities below this line

                if (entity.needsUpdate()) {
                    entity.markNeedsUpdateComplete();

                    this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entity));
                }
            }

            // Update the entity grid
            this.getRoom().getEntities().replaceEntityGrid(entityGrid);


            TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

            if(span.toMilliseconds() > 250)
                log.info("ProcessComponent process took: " + span.toMilliseconds() + "ms to execute.");
        } catch (Exception e) {
            log.error("Error during room process", e);
        }
    }

    protected void processPlayerEntity(PlayerEntity entity) {
        // Cleanup if the entity is offline

        // Copy the current position before updating
        Position3D currentPosition = new Position3D(entity.getPosition());

        if (entity.getPlayer() == null || entity.getRoom() == null) {
            this.room.getEntities().removeEntity(entity);
            this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entity));
        }

        // Handle flood
        if (entity.getPlayer().floodTime >= 0.5) {
            entity.getPlayer().floodTime -= 0.5;

            if (entity.getPlayer().floodTime < 0) {
                entity.getPlayer().floodTime = 0;
            }
        }

        if(entity.handItemNeedsRemove() && entity.getHandItem() != 0) {
            entity.carryItem(0);
            entity.setHandItemTimer(0);
        }

        // Handle signs
        if (entity.hasStatus("sign") && !entity.isDisplayingSign()) {
            entity.removeStatus("sign");
            entity.markNeedsUpdate();
        }

        // Handle expiring effects
        if (entity.getCurrentEffect() != null) {
            entity.getCurrentEffect().decrementDuration();

            if (entity.getCurrentEffect().getDuration() == 0 && entity.getCurrentEffect().expires()) {
                entity.applyEffect(null);
            }

            if (entity.getCurrentEffect() != null && entity.getCurrentEffect().isItemEffect()) {
                boolean needsRemove = true;

                for (FloorItem item : this.getRoom().getItems().getItemsOnSquare(currentPosition.getX(), currentPosition.getY())) {
                    if(item.getDefinition().getEffectId() == entity.getCurrentEffect().getEffectId()) {
                        needsRemove = false;
                    }
                }

                if(needsRemove) {
                    entity.applyEffect(null);
                }
            }
        }

        if (entity.hasStatus("mv")) {
            entity.removeStatus("mv");
            entity.markNeedsUpdate();
        }

        // Check if we are wanting to walk to a location
        if (entity.getWalkingPath() != null) {
            entity.setProcessingPath(new ArrayList<>(entity.getWalkingPath()));

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

            for (FloorItem item : this.getRoom().getItems().getItemsOnSquare(currentPosition.getX(), currentPosition.getY())) {
                item.setNeedsUpdate(true, InteractionAction.ON_WALK, entity, 0);

                if (this.getRoom().getWired().trigger(TriggerType.OFF_FURNI, item.getId(), entity)) {
                    // idk what to do here for this trigger but ya
                }
            }

            if (entity.hasStatus("sit")) {
                entity.removeStatus("sit");
            }

            // Create the new position
            Position3D newPosition = new Position3D();
            newPosition.setX(entity.getPositionToSet().getX());
            newPosition.setY(entity.getPositionToSet().getY());
            newPosition.setZ(entity.getPositionToSet().getZ());

            //entity.getPlayer().getSession().send(TalkMessageComposer.compose(entity.getVirtualId(), "X: " + newPosition.getX() + ", Y: " + newPosition.getY() + ", Rot: " + entity.getBodyRotation(), 0, 0));

            List<FloorItem> itemsOnSq = this.getRoom().getItems().getItemsOnSquare(entity.getPositionToSet().getX(), entity.getPositionToSet().getY());

            // Apply sit
            for (FloorItem item : itemsOnSq) {
                item.setNeedsUpdate(true, InteractionAction.ON_WALK, entity, 1);

                if (item.getDefinition().canSit) {
                    double height = item.getDefinition().getHeight();

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

                /*if (this.getRoom().getWired().trigger(TriggerType.ON_FURNI, item.getId(), entity)) {
                    // idk what to do here for this trigger but ya
                }*/
            }

            entity.updateAndSetPosition(null);
            entity.setPosition(newPosition);

            for (FloorItem item : itemsOnSq) {
                if (this.getRoom().getWired().trigger(TriggerType.ON_FURNI, item.getId(), entity)) {
                    // idk what to do here for this trigger but ya
                }
            }
        }

        if (entity.isWalking()) {
            Square nextSq = entity.getProcessingPath().get(0);

            if (entity.getProcessingPath().size() > 1)
                entity.setFutureSquare(entity.getProcessingPath().get(1));

            entity.getProcessingPath().remove(nextSq);

            boolean isLastStep = (entity.getProcessingPath().size() == 0);

            if (nextSq != null && entity.getRoom().getMapping().isValidStep(entity.getPosition(), new Position3D(nextSq.x, nextSq.y, 0), isLastStep)) {
                Position3D currentPos = entity.getPosition() != null ? entity.getPosition() : new Position3D(0, 0, 0);
                entity.setBodyRotation(Position3D.calculateRotation(currentPos.getX(), currentPos.getY(), nextSq.x, nextSq.y, entity.isMoonwalking()));
                entity.setHeadRotation(entity.getBodyRotation());

                double height = this.getRoom().getModel().getSquareHeight()[nextSq.x][nextSq.y];

                boolean isCancelled = false;

                for (FloorItem item : this.getRoom().getItems().getItemsOnSquare(nextSq.x, nextSq.y)) {
                    if (item.getDefinition().getInteraction().equals("gate") && item.getExtraData().equals("0"))
                        isCancelled = true;

                    //height += item.getHeight();

                    if(!item.getDefinition().canSit)
                        height += item.getDefinition().getHeight();

                    item.setNeedsUpdate(true, InteractionAction.ON_PRE_WALK, entity, 0);
                }

                if (!isCancelled) {
                    entity.addStatus("mv", String.valueOf(nextSq.x).concat(",").concat(String.valueOf(nextSq.y)).concat(",").concat(String.valueOf(height)));

                    if (entity.hasStatus("sit")) {
                        entity.removeStatus("sit");
                    }

                    if (entity.hasStatus("lay")) {
                        entity.removeStatus("lay");
                    }

                    entity.updateAndSetPosition(new Position3D(nextSq.x, nextSq.y, height));
                    entity.markNeedsUpdate();
                } else {
                    if(entity.getWalkingPath() != null)
                        entity.getWalkingPath().clear();

                    entity.getProcessingPath().clear();
                }
            }
        }

        // Remove entities from room
        for (PlayerEntity entity0 : leavingRoom) {
            entity0.leaveRoom(false, false, true);
        }
    }

    protected void processBotEntity(BotEntity entity) {
        int chance = RandomInteger.getRandom(1, 6);

        if (chance == 1) {
            if (!entity.isWalking()) {
                int x = RandomInteger.getRandom(0, this.getRoom().getModel().getSizeX());
                int y = RandomInteger.getRandom(0, this.getRoom().getModel().getSizeY());

                if (this.getRoom().getMapping().isValidStep(entity.getPosition(), new Position3D(x, y, 0d), true)) {
                    entity.moveTo(x, y);
                }
            }
        }

        if (entity.getCycleCount() == entity.getData().getChatDelay() * 2) {
            if(entity.getData().getMessages().length > 0) {
                int messageKey = RandomInteger.getRandom(0, entity.getData().getMessages().length - 1);
                String message = entity.getData().getMessages()[messageKey];

                if (message != null && !message.isEmpty()) {
                    this.getRoom().getEntities().broadcastMessage(ShoutMessageComposer.compose(entity.getVirtualId(), message, 0, 1));
                }
            }

            entity.resetCycleCount();
        }

        entity.incrementCycleCount();

        if (entity.hasStatus("mv")) {
            entity.removeStatus("mv");
            entity.markNeedsUpdate();
        }

        // Check if we are wanting to walk to a location
        if (entity.getWalkingPath() != null) {
            entity.setProcessingPath(new ArrayList<>(entity.getWalkingPath()));

            // Clear the walking path now we have a goal set
            entity.getWalkingPath().clear();
            entity.setWalkingPath(null);
        }

        // Do we have a position to set from previous moves?
        if (entity.getPositionToSet() != null) {
            // Copy the current position before updating
            Position3D currentPosition = new Position3D(entity.getPosition());

            if (entity.hasStatus("sit")) {
                entity.removeStatus("sit");
            }

            // Create the new position
            Position3D newPosition = new Position3D();
            newPosition.setX(entity.getPositionToSet().getX());
            newPosition.setY(entity.getPositionToSet().getY());
            newPosition.setZ(entity.getPositionToSet().getZ());

            //entity.getPlayer().getSession().send(TalkMessageComposer.compose(entity.getVirtualId(), "X: " + newPosition.getX() + ", Y: " + newPosition.getY() + ", Rot: " + entity.getBodyRotation(), 0,0));

            List<FloorItem> itemsOnSq = this.getRoom().getItems().getItemsOnSquare(entity.getPositionToSet().getX(), entity.getPositionToSet().getY());

            // Apply sit
            for (FloorItem item : itemsOnSq) {

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

            entity.updateAndSetPosition(null);
            entity.setPosition(newPosition);
        }

        if (entity.isWalking()) {
            Square nextSq = entity.getProcessingPath().get(0);

            if (entity.getProcessingPath().size() > 1)
                entity.setFutureSquare(entity.getProcessingPath().get(1));

            entity.getProcessingPath().remove(nextSq);

            boolean isLastStep = (entity.getProcessingPath().size() == 0);

            if (nextSq != null && entity.getRoom().getMapping().isValidStep(entity.getPosition(), new Position3D(nextSq.x, nextSq.y, 0), isLastStep)) {
                Position3D currentPos = entity.getPosition() != null ? entity.getPosition() : new Position3D(0, 0, 0);
                entity.setBodyRotation(Position3D.calculateRotation(currentPos.getX(), currentPos.getY(), nextSq.x, nextSq.y, entity.isMoonwalking()));
                entity.setHeadRotation(entity.getBodyRotation());

                double height = this.getRoom().getModel().getSquareHeight()[nextSq.x][nextSq.y];

                boolean isCancelled = false;

                for (FloorItem item : this.getRoom().getItems().getItemsOnSquare(nextSq.x, nextSq.y)) {
                    if (item.getDefinition().getInteraction().equals("gate") && item.getExtraData().equals("0"))
                        isCancelled = true;

                    height += item.getHeight();
                }

                if (!isCancelled) {
                    entity.addStatus("mv", String.valueOf(nextSq.x).concat(",").concat(String.valueOf(nextSq.y)).concat(",").concat(String.valueOf(height)));

                    if (entity.hasStatus("sit")) {
                        entity.removeStatus("sit");
                    }

                    if (entity.hasStatus("lay")) {
                        entity.removeStatus("lay");
                    }

                    entity.updateAndSetPosition(new Position3D(nextSq.x, nextSq.y, height));
                    entity.markNeedsUpdate();
                } else {
                    if(entity.getWalkingPath() != null)
                        entity.getWalkingPath().clear();
                    if(entity.getProcessingPath() != null)
                        entity.getProcessingPath().clear();
                }
            }
        }
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

    public Room getRoom() {
        return this.room;
    }
}