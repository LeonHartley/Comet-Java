package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.GateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.TeleportPadFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupGateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOffFurni;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOnFurni;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.game.rooms.types.mapping.Tile;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.IdleStatusMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.RandomInteger;
import com.cometproject.server.utilities.TimeSpan;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class ProcessComponent implements CometTask {
    private RoomInstance room;

    private Logger log;
    private ScheduledFuture processFuture;
    private boolean active = false;

    private boolean adaptiveProcessTimes = false;
    private List<Long> processTimes;

    public ProcessComponent(RoomInstance room) {
        this.room = room;
        this.log = Logger.getLogger("Room Process [" + room.getData().getName() + "]");

        this.adaptiveProcessTimes = CometSettings.adaptiveEntityProcessDelay;
    }

    public void tick() {
        if (!this.active) {
            return;
        }

        long timeStart = System.currentTimeMillis();

        try {
            this.getRoom().tick();
        } catch (Exception e) {
            log.error("Error while cycling room: " + room.getData().getId() + ", " + room.getData().getName(), e);
        }

        try {
            Map<Integer, GenericEntity> entities = this.room.getEntities().getAllEntities();

            List<PlayerEntity> playersToRemove = new ArrayList<>();
            List<GenericEntity> entitiesToUpdate = new ArrayList<>();

            for (GenericEntity entity : entities.values()) {
                if (entity.getEntityType() == RoomEntityType.PLAYER) {
                    PlayerEntity playerEntity = (PlayerEntity) entity;

                    try {
                        if (playerEntity.getPlayer() == null || playerEntity.getPlayer().isDisposed || playerEntity.getPlayer().getSession() == null) {
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

                if ((entity.needsUpdate() && !entity.needsUpdateCancel() || entity.needsForcedUpdate) && entity.isVisible()) {
                    if (entity.needsForcedUpdate && entity.updatePhase == 1) {
                        entity.needsForcedUpdate = false;
                        entity.updatePhase = 0;

                        entitiesToUpdate.add(entity);
                    } else if (entity.needsForcedUpdate) {
                        if (entity.hasStatus(RoomEntityStatus.MOVE)) {
                            entity.removeStatus(RoomEntityStatus.MOVE);
                        }

                        entity.updatePhase = 1;
                        entitiesToUpdate.add(entity);
                    } else {
                        entity.markUpdateComplete();
                        entitiesToUpdate.add(entity);
                    }
                }
            }

            // only send the updates if we need to
            if (entitiesToUpdate.size() > 0) {
                this.getRoom().getEntities().broadcastMessage(new AvatarUpdateMessageComposer(entitiesToUpdate));
            }

            for (GenericEntity entity : entitiesToUpdate) {
                if (entity.updatePhase == 1) continue;

                if (this.updateEntityStuff(entity) && entity instanceof PlayerEntity) {
                    playersToRemove.add((PlayerEntity) entity);
                }
            }

            for (PlayerEntity entity : playersToRemove) {
                entity.leaveRoom(entity.getPlayer() == null, false, true);
            }

            playersToRemove.clear();
            entitiesToUpdate.clear();

        } catch (Exception e) {
            log.warn("Error during room entity processing", e);
        }

        TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

        if (this.getProcessTimes() != null) {
            if (this.getProcessTimes().size() < 30)
                this.getProcessTimes().add(span.toMilliseconds());
        }

        if(this.adaptiveProcessTimes) {
            CometThreadManager.getInstance().executeSchedule(this, 500 - span.toMilliseconds(), TimeUnit.MILLISECONDS);
        }
    }

    public void start() {
        if (RoomInstance.useCycleForEntities) {
            this.active = true;
            return;
        }

        if (this.active) {
            stop();
        }

        if(this.adaptiveProcessTimes) {
            CometThreadManager.getInstance().executeSchedule(this, 500, TimeUnit.MILLISECONDS);
        } else {
            this.processFuture = CometThreadManager.getInstance().executePeriodic(this, 500, 500, TimeUnit.MILLISECONDS);
        }

        this.active = true;

        log.debug("Processing started");
    }

    public void stop() {
        if (RoomInstance.useCycleForEntities) {
            this.active = false;
            return;
        }

        if (this.getProcessTimes() != null) {
            this.getProcessTimes().clear();
        }

        if (this.processFuture != null) {
            this.active = false;

            if(!this.adaptiveProcessTimes)
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
                boolean leaveRoom = true;
                final List<RoomItemFloor> floorItemsAtDoor = this.getRoom().getItems().getItemsOnSquare(entity.getPositionToSet().getX(), entity.getPositionToSet().getY());

                if (!floorItemsAtDoor.isEmpty()) {
                    for (RoomItemFloor floorItem : floorItemsAtDoor) {
                        if (floorItem instanceof TeleportPadFloorItem) leaveRoom = false;
                    }
                }

                if (leaveRoom) {
                    entity.updateAndSetPosition(null);
                    return true;
                }
            }

            if (entity.hasStatus(RoomEntityStatus.SIT)) {
                entity.removeStatus(RoomEntityStatus.SIT);
            }

            // Create the new position
            Position newPosition = entity.getPositionToSet().copy();

            List<RoomItemFloor> itemsOnSq = this.getRoom().getItems().getItemsOnSquare(entity.getPositionToSet().getX(), entity.getPositionToSet().getY());
            List<RoomItemFloor> itemsOnOldSq = this.getRoom().getItems().getItemsOnSquare(entity.getPosition().getX(), entity.getPosition().getY());

            final Tile oldTile = this.getRoom().getMapping().getTile(entity.getPosition().getX(), entity.getPosition().getY());
            final Tile newTile = this.getRoom().getMapping().getTile(newPosition.getX(), newPosition.getY());

            if (oldTile != null) {
                oldTile.getEntities().remove(entity);
            }

            entity.updateAndSetPosition(null);
            entity.setPosition(newPosition);

            if (newTile != null) {
                newTile.getEntities().add(entity);
            }

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

                item.onEntityStepOn(entity);
                WiredTriggerWalksOnFurni.executeTriggers(entity, item);
            }

            if (newTile != null && newTile.getTopItem() != 0) {
                RoomItemFloor topItem = this.getRoom().getItems().getFloorItem(newTile.getTopItem());

                if (topItem != null) {
                    if (topItem.getDefinition().getEffectId() != 0) {
                        if (oldItem != null) {
                            if (oldItem.getDefinition().getEffectId() != topItem.getDefinition().getEffectId()) {
                                entity.applyEffect(new PlayerEffect(topItem.getDefinition().getEffectId(), true));
                            }
                        } else {
                            entity.applyEffect(new PlayerEffect(topItem.getDefinition().getEffectId(), true));
                        }
                    }
                }
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
            if (((PlayerEntity) entity).getPlayer().getRoomFloodTime() >= 0.5) {
                ((PlayerEntity) entity).getPlayer().setRoomFloodTime(((PlayerEntity) entity).getPlayer().getRoomFloodTime() - 0.5);

                if (((PlayerEntity) entity).getPlayer().getRoomFloodTime() < 0) {
                    ((PlayerEntity) entity).getPlayer().setRoomFloodTime(0);
                }
            }
        } else {
            int chance = RandomInteger.getRandom(1, (entity.hasStatus(RoomEntityStatus.SIT) || entity.hasStatus(RoomEntityStatus.LAY)) ? 20 : 6);

            if (!(entity instanceof PetEntity) || !entity.hasMount()) {
                boolean newStep = true;

                if (entity instanceof BotEntity) {
                    BotEntity botEntity = ((BotEntity) entity);

                    if (botEntity.getData().getMode().equals("relaxed")) {
                        newStep = false;
                    }
                }

                if (chance == 1 && newStep) {
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
                try {
                    if (((BotEntity) entity).getCycleCount() == ((BotEntity) entity).getData().getChatDelay() * 2) {
                        String message = ((BotEntity) entity).getData().getRandomMessage();

                        if (message != null && !message.isEmpty()) {
                            this.getRoom().getEntities().broadcastMessage(new TalkMessageComposer(entity.getId(), message, 0, 2));
                        }

                        ((BotEntity) entity).resetCycleCount();
                    }

                    ((BotEntity) entity).incrementCycleCount();
                } catch (Exception ignored) {

                }
            } else {
                // It's a pet.
                PetEntity petEntity = (PetEntity) entity;

                if (petEntity.getCycleCount() == 50) { // 25 seconds
                    int messageKey = RandomInteger.getRandom(0, ((PetEntity) entity).getData().getSpeech().length - 1);
                    String message = ((PetEntity) entity).getData().getSpeech()[messageKey];

                    if (message != null && !message.isEmpty()) {
                        if (entity.getPosition().getX() < this.getRoom().getModel().getSquareHeight().length && entity.getPosition().getY() < this.getRoom().getModel().getSquareHeight()[entity.getPosition().getX()].length) {
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
                                    this.getRoom().getEntities().broadcastMessage(new TalkMessageComposer(entity.getId(), message, 0, 0));
                                    break;
                            }
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

        if (entity instanceof PlayerEntity && entity.isIdleAndIncrement()) {
            if (entity.getIdleTime() >= 2400) {
                return true;
            } else {
                // Set idle status!
                this.room.getEntities().broadcastMessage(new IdleStatusMessageComposer(entity.getId(), true));
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

            if (isPlayer && ((PlayerEntity) entity).isKicked()) {

                if (((PlayerEntity) entity).getKickWalkStage() > 5) {
                    return true;
                }

                ((PlayerEntity) entity).increaseKickWalkStage();
            }

            entity.getProcessingPath().remove(nextSq);

            boolean isLastStep = (entity.getProcessingPath().size() == 0);

            if (nextSq != null && entity.getRoom().getMapping().isValidEntityStep(entity, entity.getPosition(), new Position(nextSq.x, nextSq.y, 0), isLastStep) || entity.isOverriden()) {
                Position currentPos = entity.getPosition() != null ? entity.getPosition() : new Position(0, 0, 0);
                Position nextPos = new Position(nextSq.x, nextSq.y);
                entity.setBodyRotation(Position.calculateRotation(currentPos.getX(), currentPos.getY(), nextSq.x, nextSq.y, entity.isMoonwalking()));
                entity.setHeadRotation(entity.getBodyRotation());

                final double mountHeight = entity.getMountedEntity() != null ? 1.0 : 0;//(entity.getMountedEntity() != null) ? (((String) entity.getAttribute("transform")).startsWith("15 ") ? 1.0 : 0.5) : 0;

                final Tile tile = this.room.getMapping().getTile(nextSq.x, nextSq.y);
                final double height = tile.getWalkHeight() + mountHeight;
                boolean isCancelled = entity.isWalkCancelled();
                boolean effectNeedsRemove = true;

                List<RoomItemFloor> preItems = this.getRoom().getItems().getItemsOnSquare(nextSq.x, nextSq.y);

                for (RoomItemFloor item : preItems) {
                    if (entity.getCurrentEffect() != null && entity.getCurrentEffect().getEffectId() == item.getDefinition().getEffectId()) {
                        if (item.getId() == tile.getTopItem()) {
                            effectNeedsRemove = false;
                        }
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
                        } else {
                            isCancelled = true;
                        }
                    } else {
                        item.onEntityPreStepOn(entity);
                    }
                }

                if (effectNeedsRemove && entity.getCurrentEffect() != null && entity.getCurrentEffect().isItemEffect()) {
                    entity.applyEffect(entity.getLastEffect());
                }

                if (this.getRoom().getEntities().positionHasEntity(nextPos)) {
                    final boolean allowWalkthrough = this.getRoom().getData().getAllowWalkthrough();
                    final boolean isFinalStep = entity.getWalkingGoal().equals(nextPos);

                    if (isFinalStep && allowWalkthrough) {
                        isCancelled = true;
                    } else if (!allowWalkthrough) {
                        isCancelled = true;
                    }
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
        } else {
            if (isPlayer && ((PlayerEntity) entity).isKicked())
                return true;
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

    public boolean isActive() {
        return this.active;
    }

    public RoomInstance getRoom() {
        return this.room;
    }

    public List<Long> getProcessTimes() {
        return processTimes;
    }

    public void setProcessTimes(List<Long> processTimes) {
        this.processTimes = processTimes;
    }
}