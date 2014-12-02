package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.items.types.floor.GateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupGateFloorItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOffFurni;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOnFurni;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.logging.sentry.SentryDispatcher;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.IdleStatusMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.ShoutMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.utilities.RandomInteger;
import com.cometproject.server.utilities.TimeSpan;
import javolution.util.FastMap;
import net.kencochrane.raven.event.Event;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ProcessComponent implements CometTask {
    private Room room;

    private Logger log;
    private ScheduledFuture processFuture;
    private boolean active = false;

    public ProcessComponent(Room room) {
        this.room = room;
        this.log = Logger.getLogger("Room Process [" + room.getData().getName() + "]");
    }

    public void tick() {
        if (!this.active) {
            return;
        }

        try {
            long timeStart = System.currentTimeMillis();

            Map<Integer, GenericEntity> entities = this.room.getEntities().getAllEntities();

            List<GenericEntity>[][] entityGrid = new ArrayList[this.getRoom().getModel().getSizeX()][this.getRoom().getModel().getSizeY()];

            List<PlayerEntity> playersToRemove = new ArrayList<>();
            List<GenericEntity> entitiesToUpdate = new ArrayList<>();

            for (GenericEntity entity : entities.values()) {
                if (entity.getEntityType() == RoomEntityType.PLAYER) {
                    PlayerEntity playerEntity = (PlayerEntity) entity;

                    try {
                        if (playerEntity.getPlayer() == null) {
                            playersToRemove.add(playerEntity);
                            continue;
                        }
                    } catch (Exception e) {
                        log.warn("Failed to remove null player from room - user data was null");
                        continue;
                    }

                    boolean playerNeedsRemove = processEntity(playerEntity);

                    if (playerNeedsRemove) {
                        playersToRemove.add(playerEntity);
                    }
                } else if (entity.getEntityType() == RoomEntityType.BOT) {
                    // do anything special here for bots?
                    processEntity(entity);
                } else if (entity.getEntityType() == RoomEntityType.PET) {
                    // do anything special here for pets?
                    processEntity(entity);
                }

                // Create the new entity grid
                if (entityGrid[entity.getPosition().getX()][entity.getPosition().getY()] == null) {
                    entityGrid[entity.getPosition().getX()][entity.getPosition().getY()] = new ArrayList<>();
                }

                entityGrid[entity.getPosition().getX()][entity.getPosition().getY()].add(entity);

                if ((entity.needsUpdate() && !entity.needsUpdateCancel() || entity.needsForcedUpdate) && entity.isVisible()) {
                    if (entity.needsForcedUpdate && entity.updatePhase == 1) {
                        entity.needsForcedUpdate = false;
                        entity.updatePhase = 0;

                        entitiesToUpdate.add(entity);
                    } else if (entity.needsForcedUpdate) {
                        entity.updatePhase = 1;
                    } else {
                        entity.markUpdateComplete();
                        entitiesToUpdate.add(entity);
                    }
                }
            }

            // Update the entity grid
            this.getRoom().getEntities().replaceEntityGrid(entityGrid);

            // only send the updates if we need to
            if (entitiesToUpdate.size() > 0)
                this.getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(entitiesToUpdate));

            for (GenericEntity entity : entitiesToUpdate) {
                if (this.updateEntityStuff(entity) && entity instanceof PlayerEntity) {
                    playersToRemove.add((PlayerEntity) entity);
                }
            }

            for (PlayerEntity entity : playersToRemove) {
                entity.leaveRoom(entity.getPlayer() == null, false, true);
            }

            playersToRemove.clear();
            entitiesToUpdate.clear();

            TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

            if (span.toMilliseconds() > 400)
                log.info("ProcessComponent process took: " + span.toMilliseconds() + "ms to execute.");
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            this.handleSupressedExceptions(e);
        } catch (Exception e) {
            log.warn("Error during room entity processing", e);
        }
    }

    public void start() {
        if (Room.useCycleForEntities) {
            this.active = true;
            return;
        }

        if (this.active) {
            stop();
        }

        this.processFuture = Comet.getServer().getThreadManagement().executePeriodic(this, 500, 500, TimeUnit.MILLISECONDS);
        this.active = true;

        log.debug("Processing started");
    }

    public void stop() {
        if (Room.useCycleForEntities) {
            this.active = false;
            return;
        }

        if (this.processFuture != null) {
            this.active = false;
            this.processFuture.cancel(false);

            log.debug("Processing stopped");
        }
    }

    @Override
    public void run() {
        this.tick();
    }

    private boolean updateEntityStuff(GenericEntity entity) {
        if (entity.getPositionToSet() != null) {
            if ((entity.getPositionToSet().getX() == this.room.getModel().getDoorX()) && (entity.getPositionToSet().getY() == this.room.getModel().getDoorY())) {
                entity.updateAndSetPosition(null);
                return true;
            }

            if (entity.hasStatus(RoomEntityStatus.SIT)) {
                entity.removeStatus(RoomEntityStatus.SIT);
            }

            // Create the new position
            Position newPosition = entity.getPositionToSet().copy();

            List<RoomItemFloor> itemsOnSq = this.getRoom().getItems().getItemsOnSquare(entity.getPositionToSet().getX(), entity.getPositionToSet().getY());
            List<RoomItemFloor> itemsOnOldSq = this.getRoom().getItems().getItemsOnSquare(entity.getPosition().getX(), entity.getPosition().getY());

            entity.updateAndSetPosition(null);
            entity.setPosition(newPosition);

            // Step off
            for (RoomItemFloor item : itemsOnOldSq) {
                if (!itemsOnSq.contains(item)) {
                    item.onEntityStepOff(entity);
                    WiredTriggerWalksOffFurni.executeTriggers(entity, item);
                }
            }

            // Step-on
            RoomItemFloor oldItem = null;
            int index0 = 0;

            for (RoomItemFloor item : itemsOnSq) {
                if (itemsOnOldSq.size() > index0) {
                    oldItem = itemsOnOldSq.get(index0);
                }

                index0++;

                if (item.getDefinition().getEffectId() != 0) {
                    if (oldItem != null) {
                        if (oldItem.getDefinition().getEffectId() != item.getDefinition().getEffectId()) {
                            entity.applyEffect(new PlayerEffect(item.getDefinition().getEffectId(), true));
                        }
                    } else {
                        entity.applyEffect(new PlayerEffect(item.getDefinition().getEffectId(), true));
                    }
                }

//                if (!itemsOnOldSq.contains(item)) {
                    item.onEntityStepOn(entity);
                    WiredTriggerWalksOnFurni.executeTriggers(entity, item);
//                }
            }
        }

        return false;
    }

    protected boolean processEntity(GenericEntity entity) {
        boolean isPlayer = entity instanceof PlayerEntity;

        if (isPlayer && ((PlayerEntity) entity).getPlayer() == null || entity.getRoom() == null) {
            return true; // adds it to the to remove list automatically..
        }

        if (isPlayer) {
            // Handle flood
            if (((PlayerEntity) entity).getPlayer().getFloodTime() >= 0.5) {
                ((PlayerEntity) entity).getPlayer().setFloodTime(((PlayerEntity) entity).getPlayer().getFloodTime() - 0.5);

                if (((PlayerEntity) entity).getPlayer().getFloodTime() < 0) {
                    ((PlayerEntity) entity).getPlayer().setFloodTime(0);
                }
            }
        } else {
            int chance = RandomInteger.getRandom(1, (entity.hasStatus(RoomEntityStatus.SIT) || entity.hasStatus(RoomEntityStatus.LAY)) ? 20 : 6);

            if(!(entity instanceof PetEntity) || !((PetEntity) entity).hasMount()) {
                if (chance == 1) {
                    if (!entity.isWalking()) {
                        int x = RandomInteger.getRandom(0, this.getRoom().getModel().getSizeX());
                        int y = RandomInteger.getRandom(0, this.getRoom().getModel().getSizeY());

                        if (this.getRoom().getMapping().isValidStep(entity.getPosition(), new Position(x, y, 0d), true) && x != this.getRoom().getModel().getDoorX() && y != this.getRoom().getModel().getDoorY()) {
                            entity.moveTo(x, y);
                        }
                    }
                }
            }

            if (entity instanceof BotEntity) {
                if (((BotEntity) entity).getCycleCount() == ((BotEntity) entity).getData().getChatDelay() * 2) {
                    String message = ((BotEntity) entity).getData().getRandomMessage();

                    if (message != null && !message.isEmpty()) {
                        this.getRoom().getEntities().broadcastMessage(ShoutMessageComposer.compose(entity.getId(), message, 0, 2));
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
                        final String status = "" + this.room.getModel().getSquareHeight()[entity.getPosition().getX()][entity.getPosition().getY()];

                        switch (message) {
                            case "sit":
                                entity.addStatus(RoomEntityStatus.SIT, status);
                                entity.markNeedsUpdate();
                                break;

                            case "lay":
                                entity.addStatus(RoomEntityStatus.LAY, status);
                                entity.markNeedsUpdate();
                                break;

                            default:
                                this.getRoom().getEntities().broadcastMessage(TalkMessageComposer.compose(entity.getId(), message, 0, 0));
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
        if (entity.hasStatus(RoomEntityStatus.SIGN) && !entity.isDisplayingSign()) {
            entity.removeStatus(RoomEntityStatus.SIGN);
            entity.markNeedsUpdate();
        }

        if (entity.isIdleAndIncrement()) {
            if (entity.getIdleTime() >= 2400) {
                // Remove entity
                return true;
            } else {
                // Set idle status!
                this.room.getEntities().broadcastMessage(IdleStatusMessageComposer.compose(entity.getId(), true));
                entity.resetIdleTime();
            }
        }

        if (entity.hasStatus(RoomEntityStatus.MOVE)) {
            entity.removeStatus(RoomEntityStatus.MOVE);
            entity.markNeedsUpdate();
        }

        // Check if we are wanting to walk to a location
        if (entity.getWalkingPath() != null) {
            entity.setProcessingPath(new ArrayList<>(entity.getWalkingPath()));

            // Clear the walking path now we have a goal set
            entity.getWalkingPath().clear();
            entity.setWalkingPath(null);
        }

        if (entity.isWalking()) {
            Square nextSq = entity.getProcessingPath().get(0);

            if (entity.getProcessingPath().size() > 1)
                entity.setFutureSquare(entity.getProcessingPath().get(1));

            entity.getProcessingPath().remove(nextSq);

            boolean isLastStep = (entity.getProcessingPath().size() == 0);

            if (nextSq != null && entity.getRoom().getMapping().isValidStep(entity.getPosition(), new Position(nextSq.x, nextSq.y, 0), isLastStep) || entity.isOverriden()) {
                Position currentPos = entity.getPosition() != null ? entity.getPosition() : new Position(0, 0, 0);
                entity.setBodyRotation(Position.calculateRotation(currentPos.getX(), currentPos.getY(), nextSq.x, nextSq.y, entity.isMoonwalking()));
                entity.setHeadRotation(entity.getBodyRotation());

                final double height = this.room.getMapping().getTile(nextSq.x, nextSq.y).getWalkHeight() + (entity.getMountedEntity() != null ? 1.0 : 0);
                boolean isCancelled = entity.isWalkCancelled();
                boolean effectNeedsRemove = true;

                List<RoomItemFloor> preItems = this.getRoom().getItems().getItemsOnSquare(nextSq.x, nextSq.y);

                for (RoomItemFloor item : preItems) {
                    if (entity.getCurrentEffect() != null
                            && entity.getCurrentEffect().getEffectId() == item.getDefinition().getEffectId()) {
                        effectNeedsRemove = false;
                    }

                    if (item instanceof GateFloorItem && !((GateFloorItem) item).isOpen()) {
                        isCancelled = true;
                    } else if (item instanceof GroupGateFloorItem) {
                        if (isPlayer) {
                            if (((PlayerEntity) entity).getPlayer().getGroups().contains(((GroupGateFloorItem) item).getGroupId())) {
                                item.onEntityPreStepOn(entity);
                            } else {
                                isCancelled = true;
                            }
                        }
                    } else {
                        item.onEntityPreStepOn(entity);
                    }
                }

                if (effectNeedsRemove && entity.getCurrentEffect() != null && entity.getCurrentEffect().isItemEffect()) {
                    entity.applyEffect(entity.getLastEffect());
                }

                if (!isCancelled) {
                    entity.addStatus(RoomEntityStatus.MOVE, String.valueOf(nextSq.x).concat(",").concat(String.valueOf(nextSq.y)).concat(",").concat(String.valueOf(height)));

                    if (entity.hasStatus(RoomEntityStatus.SIT)) {
                        entity.removeStatus(RoomEntityStatus.SIT);
                    }

                    if (entity.hasStatus(RoomEntityStatus.LAY)) {
                        entity.removeStatus(RoomEntityStatus.LAY);
                    }

                    entity.updateAndSetPosition(new Position(nextSq.x, nextSq.y, height));
                    entity.markNeedsUpdate();
                } else {
                    if (entity.getWalkingPath() != null) {
                        entity.getWalkingPath().clear();
                    }
                    entity.getProcessingPath().clear();
                    entity.setWalkCancelled(false);
                }
            } else {
                if (entity.getWalkingPath() != null) {
                    entity.getWalkingPath().clear();
                }
                entity.getProcessingPath().clear();
            }
        }

        // Handle expiring effects
        if (entity.getCurrentEffect() != null) {
            entity.getCurrentEffect().decrementDuration();

            if (entity.getCurrentEffect().getDuration() == 0 && entity.getCurrentEffect().expires()) {
                entity.applyEffect(entity.getLastEffect() != null ? entity.getLastEffect() : null);

                if (entity.getLastEffect() != null)
                    entity.setLastEffect(null);
            }
        }

        if (entity.isWalkCancelled()) {
            entity.setWalkCancelled(false);
        }

        return false;
    }

    protected void handleSupressedExceptions(Exception e) {
//        SentryDispatcher.getInstance().dispatchException("entityProcess", "Exception during entity process", e, Event.Level.ERROR, new FastMap<String, Object>() {{
//            put("Room ID", room.getId());
//            put("Room Name", room.getData().getName());
//        }});

        log.error("Error while processing entity", e);
    }

    public boolean isActive() {
        return this.active;
    }

    public Room getRoom() {
        return this.room;
    }
}