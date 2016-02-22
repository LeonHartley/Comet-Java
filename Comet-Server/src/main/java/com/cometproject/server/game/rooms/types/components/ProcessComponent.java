package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.quests.types.QuestType;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.GateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.TeleportPadFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupGateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.horse.HorseJumpFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOffFurni;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOnFurni;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.TimeSpan;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class ProcessComponent implements CometTask {
    private Room room;

    private Logger log;
    private ScheduledFuture processFuture;
    private boolean active = false;

    private boolean adaptiveProcessTimes = false;
    private List<Long> processTimes;

    private long lastProcess = 0;

    private boolean isProcessing = false;

    public ProcessComponent(Room room) {
        this.room = room;
        this.log = Logger.getLogger("Room Process [" + room.getData().getName() + ", #" + room.getId() + "]");

        this.adaptiveProcessTimes = CometSettings.adaptiveEntityProcessDelay;
    }

    public void tick() {
        if (!this.active) {
            return;
        }

        if (this.isProcessing) return;

        this.isProcessing = true;

        long timeSinceLastProcess = this.lastProcess == 0 ? 0 : (System.currentTimeMillis() - this.lastProcess);
        this.lastProcess = System.currentTimeMillis();

        if (this.getProcessTimes() != null && this.getProcessTimes().size() < 30) {
            log.info("Time since last process: " + timeSinceLastProcess + "ms");
        }

        long timeStart = System.currentTimeMillis();

        try {
            this.getRoom().tick();
        } catch (Exception e) {
            log.error("Error while cycling room: " + room.getData().getId() + ", " + room.getData().getName(), e);
        }

        try {
            Map<Integer, RoomEntity> entities = this.room.getEntities().getAllEntities();

            List<PlayerEntity> playersToRemove = new ArrayList<>();
            List<RoomEntity> entitiesToUpdate = new ArrayList<>();

            for (RoomEntity entity : entities.values()) {
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
                    if (entity.getMountedEntity() == null) {
                        // do anything special here for pets?
                        processEntity(entity);
                    }
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
                        if (entity instanceof PlayerEntity) {
                            if (entity.getMountedEntity() != null) {
                                processEntity(entity.getMountedEntity());

                                entity.getMountedEntity().markUpdateComplete();
                                entitiesToUpdate.add(entity.getMountedEntity());
                            }
                        }

                        entity.markUpdateComplete();
                        entitiesToUpdate.add(entity);
                    }
                }
            }

            // only send the updates if we need to
            if (entitiesToUpdate.size() > 0) {
                this.getRoom().getEntities().broadcastMessage(new AvatarUpdateMessageComposer(entitiesToUpdate));
            }

            for (RoomEntity entity : entitiesToUpdate) {
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

//            log.debug("Room processing took " + (System.currentTimeMillis() - timeStart) + "ms");
        } catch (Exception e) {
            log.warn("Error during room entity processing", e);
        }

        TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

        if (this.getProcessTimes() != null) {
            if (this.getProcessTimes().size() < 30)
                this.getProcessTimes().add(span.toMilliseconds());
        }

        if (this.adaptiveProcessTimes) {
            CometThreadManager.getInstance().executeSchedule(this, 500 - span.toMilliseconds(), TimeUnit.MILLISECONDS);
        }

        this.isProcessing = false;
    }

    public void start() {
        if (Room.useCycleForEntities) {
            this.active = true;
            return;
        }

        if (this.active) {
            stop();
        }

        if (this.adaptiveProcessTimes) {
            CometThreadManager.getInstance().executeSchedule(this, 500, TimeUnit.MILLISECONDS);
        } else {
            this.processFuture = CometThreadManager.getInstance().executePeriodic(this, 500, 500, TimeUnit.MILLISECONDS);
        }

        this.active = true;

        log.debug("Processing started");
    }

    public void stop() {
        if (Room.useCycleForEntities) {
            this.active = false;
            return;
        }

        if (this.getProcessTimes() != null) {
            this.getProcessTimes().clear();
        }

        if (this.processFuture != null) {
            this.active = false;

            if (!this.adaptiveProcessTimes)
                this.processFuture.cancel(false);

            log.debug("Processing stopped");
        }
    }

    @Override
    public void run() {
        this.tick();
    }

    public void setDelay(int time) {
        this.processFuture.cancel(false);
        this.processFuture = CometThreadManager.getInstance().executePeriodic(this, 0, time, TimeUnit.MILLISECONDS);
    }

    private boolean updateEntityStuff(RoomEntity entity) {
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
            Position oldPosition = entity.getPosition().copy();

            List<RoomItemFloor> itemsOnSq = this.getRoom().getItems().getItemsOnSquare(entity.getPositionToSet().getX(), entity.getPositionToSet().getY());
            List<RoomItemFloor> itemsOnOldSq = this.getRoom().getItems().getItemsOnSquare(entity.getPosition().getX(), entity.getPosition().getY());

            final RoomTile oldTile = this.getRoom().getMapping().getTile(entity.getPosition().getX(), entity.getPosition().getY());
            final RoomTile newTile = this.getRoom().getMapping().getTile(newPosition.getX(), newPosition.getY());

            if (oldTile != null) {
                oldTile.getEntities().remove(entity);
            }

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

                if (entity instanceof PlayerEntity) {
                    PlayerEntity playerEntity = ((PlayerEntity) entity);

                    if (playerEntity.getPlayer() != null && playerEntity.getPlayer().getData().getQuestId() != 0 && playerEntity.getPlayer().getQuests() != null)
                        ((PlayerEntity) entity).getPlayer().getQuests().progressQuest(QuestType.EXPLORE_FIND_ITEM, item.getDefinition().getSpriteId());
                }
            }

            if (entity.getFollowingEntities().size() != 0) {
                entity.getFollowingEntities().forEach(e -> e.moveTo(oldPosition));
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

                    topItem.onEntityStepOn(entity);
                    WiredTriggerWalksOnFurni.executeTriggers(entity, topItem);
                }
            } else if (newTile != null) {
                newTile.onEntityEntersTile(entity);
            }
        }

        return false;
    }

    private boolean processEntity(RoomEntity entity) {
        return this.processEntity(entity, false);
    }

    private boolean processEntity(RoomEntity entity, boolean isRetry) {
        boolean isPlayer = entity instanceof PlayerEntity;

        if (isPlayer && ((PlayerEntity) entity).getPlayer() == null || entity.getRoom() == null) {
            return true; // adds it to the to remove list automatically..
        }

        if (!isRetry) {
            if (isPlayer) {
                // Handle flood
                if (((PlayerEntity) entity).getPlayer().getRoomFloodTime() >= 0.5) {
                    ((PlayerEntity) entity).getPlayer().setRoomFloodTime(((PlayerEntity) entity).getPlayer().getRoomFloodTime() - 0.5);

                    if (((PlayerEntity) entity).getPlayer().getRoomFloodTime() < 0) {
                        ((PlayerEntity) entity).getPlayer().setRoomFloodTime(0);
                    }
                }
            } else {
                if (entity.getAI() != null) {
                    entity.getAI().onTick();
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
                if (entity.getIdleTime() >= 60 * CometSettings.roomIdleMinutes * 2) {
                    if (this.getRoom().getData().getOwnerId() != ((PlayerEntity) entity).getPlayerId())
                        return true;
                }
            }
        }

        if (entity.hasStatus(RoomEntityStatus.MOVE)) {
            entity.removeStatus(RoomEntityStatus.MOVE);
            entity.markNeedsUpdate();
        }

        // Check if we are wanting to walk to a location
        if (entity.getWalkingPath() != null) {
            entity.setProcessingPath(new CopyOnWriteArrayList<>(entity.getWalkingPath()));

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

                final double mountHeight = entity instanceof PlayerEntity && entity.getMountedEntity() != null ? 1.0 : 0;//(entity.getMountedEntity() != null) ? (((String) entity.getAttribute("transform")).startsWith("15 ") ? 1.0 : 0.5) : 0;

                final RoomTile tile = this.room.getMapping().getTile(nextSq.x, nextSq.y);
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

                    if(item.isMovementCancelled(entity)) {
                        isCancelled = true;
                    }

                    if(!isCancelled) {
                        item.onEntityStepOn(entity);
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

                    RoomEntity entityOnTile = this.getRoom().getMapping().getTile(nextPos.getX(), nextPos.getY()).getEntity();

                    if (entityOnTile != null && entityOnTile.getMountedEntity() != null && entityOnTile.getMountedEntity() == entity) {
                        isCancelled = false;
                    }
                }

                if (!isCancelled) {
                    entity.addStatus(RoomEntityStatus.MOVE, String.valueOf(nextSq.x).concat(",").concat(String.valueOf(nextSq.y)).concat(",").concat(String.valueOf(height)));

                    entity.removeStatus(RoomEntityStatus.SIT);
                    entity.removeStatus(RoomEntityStatus.LAY);

                    final Position newPosition = new Position(nextSq.x, nextSq.y, height);
                    entity.updateAndSetPosition(newPosition);
                    entity.markNeedsUpdate();

                    if (entity instanceof PlayerEntity && entity.getMountedEntity() != null) {
                        RoomEntity mountedEntity = entity.getMountedEntity();

                        mountedEntity.moveTo(newPosition.getX(), newPosition.getY());
                    }

                    tile.getEntities().add(entity);
                } else {
                    if (entity.getWalkingPath() != null) {
                        entity.getWalkingPath().clear();
                    }
                    entity.getProcessingPath().clear();
                    entity.setWalkCancelled(false);//
                }
            } else {
                if (entity.getWalkingPath() != null) {
                    entity.getWalkingPath().clear();
                }

                entity.getProcessingPath().clear();

                // RoomTile is blocked, let's try again!
                entity.moveTo(entity.getWalkingGoal().getX(), entity.getWalkingGoal().getY());
                return this.processEntity(entity, true);
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

    public Room getRoom() {
        return this.room;
    }

    public List<Long> getProcessTimes() {
        return processTimes;
    }

    public void setProcessTimes(List<Long> processTimes) {
        this.processTimes = processTimes;
    }
}