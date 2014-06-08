package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.avatars.effects.UserEffect;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.Square;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.RoomEntityType;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.types.TriggerType;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.IdleStatusMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.ShoutMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.storage.queries.pets.RoomPetDao;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.utilities.RandomInteger;
import com.cometproject.server.utilities.TimeSpan;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ProcessComponent implements CometTask {
    private final int ROOM_DISPOSE_TIME = Integer.parseInt(Comet.getServer().getConfig().get("comet.room.dispose.cycles"));
    private int disposeCycles = 0;

    private ArrayList<GenericEntity> usersToUpdate;

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
                this.usersToUpdate = new ArrayList<>();
            } else if (this.usersToUpdate.size() > 0) {
                this.usersToUpdate.clear();
            }

            Map<Integer, GenericEntity> entities = this.room.getEntities().getEntitiesCollection();

            List<GenericEntity>[][] entityGrid = new ArrayList[this.getRoom().getModel().getSizeX()][this.getRoom().getModel().getSizeY()];

            List<PlayerEntity> playersToRemove = new ArrayList<>();

            for (GenericEntity entity : entities.values()) {
                // Process each entity as its own
                if (entity.getEntityType() == RoomEntityType.PLAYER) {
                    PlayerEntity playerEntity = (PlayerEntity) entity;

                    try {
                        if (playerEntity.getPlayer() == null) {
                            playersToRemove.add(playerEntity);
                            return;
                        }
                    } catch(Exception e) {
                        log.warn("Failed to remove null player from room - user data was null");
                        return;
                    }

                    boolean playerNeedsRemove = processEntity(playerEntity);

                    if (playerNeedsRemove) {
                        playersToRemove.add(playerEntity);
                    }
                } else if (entity.getEntityType() == RoomEntityType.BOT) {
                    processEntity(entity);
                } else if (entity.getEntityType() == RoomEntityType.PET) {
                    processEntity(entity);
                }

                // Create the new entity grid
                if (entityGrid[entity.getPosition().getX()][entity.getPosition().getY()] == null) {
                    entityGrid[entity.getPosition().getX()][entity.getPosition().getY()] = new ArrayList<>();
                }

                entityGrid[entity.getPosition().getX()][entity.getPosition().getY()].add(entity);

                // Process anything generic for all entities below this line

                if (entity.needsUpdate()) {
                    entity.markNeedsUpdateComplete();

                    this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entity));
                }
            }

            for (PlayerEntity entity : playersToRemove) {
                entity.leaveRoom(entity.getPlayer() == null, false, true);
            }

            // Update the entity grid
            this.getRoom().getEntities().replaceEntityGrid(entityGrid);


            TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

            if (span.toMilliseconds() > 250)
                log.info("ProcessComponent process took: " + span.toMilliseconds() + "ms to execute.");
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            this.handleSupressedExceptions(e);
        } catch (Exception e) {
            log.error("Error during room process", e);
        }
    }

    protected boolean processEntity(GenericEntity entity) {

        // Copy the current position before updating
        Position3D currentPosition = new Position3D(entity.getPosition());

        boolean isPlayer = entity instanceof PlayerEntity;

        if (isPlayer && ((PlayerEntity) entity).getPlayer() == null || entity.getRoom() == null) {
            //this.room.getEntities().removeEntity(entity);
            //this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entity));
            return true;
        }

        if(isPlayer) {
            // Handle flood
            if (((PlayerEntity) entity).getPlayer().getFloodTime() >= 0.5) {
                ((PlayerEntity) entity).getPlayer().setFloodTime(((PlayerEntity) entity).getPlayer().getFloodTime() - 0.5);

                if (((PlayerEntity) entity).getPlayer().getFloodTime() < 0) {
                    ((PlayerEntity) entity).getPlayer().setFloodTime(0);
                }
            }
        } else {
            int chance = RandomInteger.getRandom(1, (entity.hasStatus("sit") || entity.hasStatus("lay")) ? 20 : 6);

            if (chance == 1) {
                if (!entity.isWalking()) {
                    int x = RandomInteger.getRandom(0, this.getRoom().getModel().getSizeX());
                    int y = RandomInteger.getRandom(0, this.getRoom().getModel().getSizeY());

                    if (this.getRoom().getMapping().isValidStep(entity.getPosition(), new Position3D(x, y, 0d), true) && x != this.getRoom().getModel().getDoorX() && y != this.getRoom().getModel().getDoorY()) {
                        entity.moveTo(x, y);
                    }
                }
            }

            if (entity instanceof BotEntity) {
                if (((BotEntity) entity).getCycleCount() == ((BotEntity) entity).getData().getChatDelay() * 2) {
                    String message = ((BotEntity) entity).getData().getRandomMessage();

                    if (message != null && !message.isEmpty()) {
                        this.getRoom().getEntities().broadcastMessage(ShoutMessageComposer.compose(entity.getVirtualId(), message, 0, 2));
                    }

                    ((BotEntity) entity).resetCycleCount();
                }

                ((BotEntity) entity).incrementCycleCount();
            } else {
                // It's a pet.
                PetEntity petEntity = (PetEntity) entity;

                if (petEntity.getCycleCount() == 50) { // 25 seconds
                    int messageKey = RandomInteger.getRandom(0, ((PetEntity) entity).getData().getSpeech().length - 1);
                    String message = ((PetEntity) entity).getData().getSpeech()[messageKey];

                    if (message != null && !message.isEmpty()) {
                        switch (message) {
                            case "sit":
                                entity.addStatus("sit", "" + this.room.getModel().getSquareHeight()[entity.getPosition().getX()][entity.getPosition().getY()]);
                                entity.markNeedsUpdate();
                                break;

                            case "lay":
                                entity.addStatus("lay", "" + this.room.getModel().getSquareHeight()[entity.getPosition().getX()][entity.getPosition().getY()]);
                                entity.markNeedsUpdate();
                                break;

                            default:
                                this.getRoom().getEntities().broadcastMessage(TalkMessageComposer.compose(entity.getVirtualId(), message, 0, 0));
                                break;
                        }
                    }

                    petEntity.resetCycleCount();
                }

                petEntity.incrementCycleCount();
            }
        }

        if (entity.handItemNeedsRemove() && entity.getHandItem() != 0) {
            entity.carryItem(0);
            entity.setHandItemTimer(0);
        }

        // Handle signs
        if (entity.hasStatus("sign") && !entity.isDisplayingSign()) {
            entity.removeStatus("sign");
            entity.markNeedsUpdate();
        }

        if (entity.isIdleAndIncrement()) {
            if (entity.getIdleTime() >= 2400) {
                // Remove entity
                return true;
            } else {
                // Set idle status!
                this.room.getEntities().broadcastMessage(IdleStatusMessageComposer.compose(entity.getVirtualId(), true));
                entity.resetIdleTime();
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

        // Do we have a position to set from previous moves?
        if (entity.getPositionToSet() != null) {
            if ((entity.getPositionToSet().getX() == this.room.getModel().getDoorX()) && (entity.getPositionToSet().getY() == this.room.getModel().getDoorY())) {
                return true;
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

            List<RoomItemFloor> itemsOnSq = this.getRoom().getItems().getItemsOnSquare(entity.getPositionToSet().getX(), entity.getPositionToSet().getY());

            // Step off
            for (RoomItemFloor item : this.getRoom().getItems().getItemsOnSquare(entity.getPosition().getX(), entity.getPosition().getY())) {
                item.onEntityStepOff(entity);

                if (this.getRoom().getWired().trigger(TriggerType.OFF_FURNI, item.getId(), entity)) {

                }
            }

            // Step-on
            for (RoomItemFloor item : itemsOnSq) {
                if(item.getDefinition().getEffectId() != 0) {
                    entity.applyEffect(new UserEffect(item.getDefinition().getEffectId(), true));
                }

                item.onEntityStepOn(entity);

                if (this.getRoom().getWired().trigger(TriggerType.ON_FURNI, item.getId(), entity)) {
                    // idk what to do here for this trigger but ya
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

            if (nextSq != null && entity.getRoom().getMapping().isValidStep(entity.getPosition(), new Position3D(nextSq.x, nextSq.y, 0), isLastStep) || entity.isOverriden()) {
                Position3D currentPos = entity.getPosition() != null ? entity.getPosition() : new Position3D(0, 0, 0);
                entity.setBodyRotation(Position3D.calculateRotation(currentPos.getX(), currentPos.getY(), nextSq.x, nextSq.y, entity.isMoonwalking()));
                entity.setHeadRotation(entity.getBodyRotation());

                double height = this.getRoom().getModel().getSquareHeight()[nextSq.x][nextSq.y];

                boolean isCancelled = false;

                boolean effectNeedsRemove = true;

                for (RoomItemFloor item : this.getRoom().getItems().getItemsOnSquare(nextSq.x, nextSq.y)) {
                    if(entity.getCurrentEffect() != null && entity.getCurrentEffect().getEffectId() == item.getDefinition().getEffectId()) {
                        effectNeedsRemove = false;
                    }

                    if (item.getDefinition().getInteraction().equals("gate") && item.getExtraData().equals("0")) {
                        isCancelled = true;
                    }
                    if (!item.getDefinition().canSit){
                        height += item.getDefinition().getHeight();
                    }
                }

                if(effectNeedsRemove) {
                    entity.applyEffect(null);
                }

                // Pre-step on
                for (RoomItemFloor item : this.getRoom().getItems().getItemsOnSquare(nextSq.x, nextSq.y)) {
                    item.onEntityPreStepOn(entity);
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
                    if (entity.getWalkingPath() != null){ entity.getWalkingPath().clear(); }
                    entity.getProcessingPath().clear();
                }
            } else {
                if (entity.getWalkingPath() != null) { entity.getWalkingPath().clear(); }

                entity.getProcessingPath().clear();
            }
        }

        // Handle expiring effects
        if (entity.getCurrentEffect() != null) {
            entity.getCurrentEffect().decrementDuration();

            if (entity.getCurrentEffect().getDuration() == 0 && entity.getCurrentEffect().expires()) {
                entity.applyEffect(null);
            }

            if (entity.getCurrentEffect() != null && entity.getCurrentEffect().isItemEffect()) {
                boolean needsRemove = true;

                //for (RoomItemFloor item : this.getRoom().getItems().getItemsOnSquare(currentPosition.getX(), currentPosition.getY())) {
                //    if (item.getDefinition().getEffectId() == entity.getCurrentEffect().getEffectId()) {
                //        needsRemove = false;
                //    }
                //}

                //if (needsRemove) {
                //    entity.applyEffect(null);
                //}
            }
        }

        return false;
    }

    protected void handleSupressedExceptions(Throwable t) {
        // TO-DO: we need log these somewhere separately so we can 'fix' these kind of errors easily..
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