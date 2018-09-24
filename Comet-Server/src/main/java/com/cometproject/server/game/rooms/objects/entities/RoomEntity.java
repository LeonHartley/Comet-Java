package com.cometproject.server.game.rooms.objects.entities;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.rooms.models.RoomTileState;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.rooms.objects.RoomFloorObject;
import com.cometproject.server.game.rooms.objects.RoomObject;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.types.EntityPathfinder;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.BotAI;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.SeatFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomEntityMovementNode;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.avatar.*;
import com.cometproject.server.storage.queries.permissions.PermissionsDao;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RoomEntity extends RoomFloorObject implements AvatarEntity {
    public int updatePhase = 0;
    public boolean needsForcedUpdate = false;
    private RoomEntityType entityType;
    private Position walkingGoal;
    private Position positionToSet;
    private int bodyRotation;
    private int headRotation;
    private List<Square> processingPath;
    private List<Square> walkingPath;
    private Square futureSquare;
    private int previousSteps = 0;
    private int idleTime;
    private int signTime;
    private int danceId;
    private PlayerEffect teamEffect;
    private PlayerEffect lastEffect;
    private PlayerEffect effect;
    private int handItem;
    private int handItemTimer;
    private boolean needsUpdate;
    private boolean isMoonwalking;
    private boolean overriden;
    private boolean isVisible;
    private boolean cancelNextUpdate;
    private boolean doorbellAnswered;
    private boolean walkCancelled = false;
    private boolean canWalk = true;
    private boolean isIdle = false;

    private final Set<RoomTile> tiles = Sets.newConcurrentHashSet();

    private boolean isRoomMuted = false;

    private long joinTime;

    private RoomEntity mountedEntity;
    private Set<RoomEntity> followingEntities = new ConcurrentHashSet<>();

    private long privateChatItemId = 0;

    private Map<RoomEntityStatus, String> statuses = new ConcurrentHashMap<>();
    private Position pendingWalk;

    private boolean fastWalkEnabled = false;
    private boolean isWarped;
    private RoomTile warpedTile;
    private boolean sendUpdateMessage = true;
    private boolean hasMount = false;

    private boolean warping;

    public RoomEntity(int identifier, Position startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        super(identifier, startPosition, roomInstance);

        if (this instanceof PlayerEntity) {
            this.entityType = RoomEntityType.PLAYER;
        } else if (this instanceof BotEntity) {
            this.entityType = RoomEntityType.BOT;
        } else if (this instanceof PetEntity) {
            this.entityType = RoomEntityType.PET;
        }

        this.bodyRotation = startBodyRotation;
        this.headRotation = startHeadRotation;

        this.idleTime = 0;
        this.signTime = 0;
        this.handItem = 0;
        this.handItemTimer = 0;

        this.danceId = 0;

        this.needsUpdate = false;
        this.isMoonwalking = false;
        this.overriden = false;
        this.isVisible = true;
        this.cancelNextUpdate = false;

        this.doorbellAnswered = false;

        if (this.getRoom().hasRoomMute()) {
            this.isRoomMuted = true;
        }

        this.joinTime = System.currentTimeMillis();
    }

    public RoomEntityType getEntityType() {
        return this.entityType;
    }

    @Override
    public Position getWalkingGoal() {
        if (this.walkingGoal == null) {
            return this.getPosition();
        } else {
            return this.walkingGoal;
        }
    }

    @Override
    public void setWalkingGoal(int x, int y) {
        this.walkingGoal = new Position(x, y, 0.0);
    }

    public void moveTo(Position position) {
        this.moveTo(position.getX(), position.getY());
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.isWarped()) {
            return;
        }

        RoomTile tile = this.getRoom().getMapping().getTile(x, y);

        if (tile == null)
            return;

        if (tile.getState() == RoomTileState.INVALID || tile.getMovementNode() == RoomEntityMovementNode.CLOSED) {
            return;
        }

//        if(this instanceof PlayerEntity) {
//            final PlayerEntity playerEntity = ((PlayerEntity) this);
//
//            if(tile.getEntities().size() != 0 && playerEntity.getGameTeam() != GameTeam.NONE) {
//                // We're playing!
//
//                final List<RoomTile> tiles = tile.getAdjacentTiles(this.getPosition());
//
//                for(RoomTile roomTiles : tiles) {
//                    if(roomTiles.getMovementNode() != RoomEntityMovementNode.CLOSED) {
//                        this.moveTo(roomTiles.getPosition());
//                        return;
//                    }
//                }
//
//            }
//        }

        this.previousSteps = 0;

        // reassign the position values if they're set to redirect
        if (tile.getRedirect() != null) {
            x = tile.getRedirect().getX();
            y = tile.getRedirect().getY();
        }

//        if (this.getPositionToSet() != null) {
//            RoomTile oldTile = this.getRoom().getMapping().getTile(this.getPosition());
//            RoomTile newTile = this.getRoom().getMapping().getTile(this.getPositionToSet());
//
//            if (oldTile != null) {
//                oldTile.getEntities().remove(this);
//            }
//
//            if (newTile != null) {
//                newTile.getEntities().add(this);
//            }
//
//            this.setPosition(this.getPositionToSet());
//        }

        // Set the goal we are wanting to achieve
        this.setWalkingGoal(x, y);

        // Create a walking path
        List<Square> path = EntityPathfinder.getInstance().makePath(this, new Position(x, y), this.getRoom().hasAttribute("disableDiagonal") ? (byte) 0 : (byte) 1, false);

        // Check returned path to see if it calculated one
        if (path == null || path.size() == 0) {
            path = EntityPathfinder.getInstance().makePath(this, new Position(x, y), this.getRoom().hasAttribute("disableDiagonal") ? (byte) 0 : (byte) 1, true);

            if (path == null || path.size() == 0) {
                // Reset the goal and return as no path was found
                this.setWalkingGoal(this.getPosition().getX(), this.getPosition().getY());
                return;
            }
        }
//
//        if(this.isFastWalkEnabled()) {
//            List<Square> newPath = new ArrayList<>();
//
//            boolean add = false;
//            for(Square square : path) {
//                if(add) {
//                    newPath.add(square);
//                    add = false;
//                } else {
//                    add = true;
//                }
//            }
//
//            path.clear();
//            path = newPath;
//        }

        // UnIdle the user and set the path (if the path has nodes it will mean the user is walking)
        this.unIdle();
        this.setWalkingPath(path);
    }

    public void sit(double height, int rotation) {
        this.removeStatus(RoomEntityStatus.LAY);

        this.addStatus(RoomEntityStatus.SIT, String.valueOf(height).replace(",", "."));
        this.setHeadRotation(getSitRotation(rotation));
        this.setBodyRotation(getSitRotation(rotation));
        this.markNeedsUpdate();
    }

    private int getSitRotation(int rotation) {
        switch (rotation) {
            case 1: {
                rotation++;
                break;
            }
            case 3: {
                rotation++;
                break;
            }
            case 5: {
                rotation++;
            }
        }
        return rotation;
    }

    public void lookTo(int x, int y, boolean body) {
        if (x == this.getPosition().getX() && y == this.getPosition().getY())
            return;

        final int currentRotation = this.bodyRotation;
        final int rotation = Position.calculateRotation(this.getPosition().getX(), this.getPosition().getY(), x, y, false);

        int rotationDifference = currentRotation - rotation;

        this.unIdle();

        if (!this.hasStatus(RoomEntityStatus.SIT) && !this.hasStatus(RoomEntityStatus.LAY)) {
            if (rotationDifference == 1 || rotationDifference == -1 || rotationDifference == -7) {
                this.setHeadRotation(rotation);
            } else if (body) {
                this.setHeadRotation(rotation);
                this.setBodyRotation(rotation);
            }

            this.markNeedsUpdate();
        }
    }

    public void lookTo(int x, int y) {
        lookTo(x, y, true);
    }

    @Override
    public Position getPositionToSet() {
        return this.positionToSet;
    }

    @Override
    public void updateAndSetPosition(Position pos) {
        this.positionToSet = pos;
    }

    @Override
    public void markPositionIsSet() {
        this.positionToSet = null;
    }

    public boolean hasPositionToSet() {
        return (this.positionToSet != null);
    }

    @Override
    public int getBodyRotation() {
        return this.bodyRotation;
    }

    @Override
    public void setBodyRotation(int rotation) {
        this.bodyRotation = rotation;
    }

    @Override
    public int getHeadRotation() {
        return this.headRotation;
    }

    @Override
    public void setHeadRotation(int rotation) {
        this.headRotation = rotation;
    }

    @Override
    public List<Square> getProcessingPath() {
        return this.processingPath;
    }

    @Override
    public void setProcessingPath(List<Square> path) {
        this.processingPath = path;
    }

    @Override
    public List<Square> getWalkingPath() {
        return this.walkingPath;
    }

    @Override
    public void setWalkingPath(List<Square> path) {
        if (this.walkingPath != null) {
            this.walkingPath.clear();
        }

        this.walkingPath = path;
    }

    @Override
    public boolean isWalking() {
        return (this.processingPath != null) && (this.processingPath.size() > 0);
    }

    @Override
    public Square getFutureSquare() {
        return this.futureSquare;
    }

    @Override
    public void setFutureSquare(Square square) {
        this.futureSquare = square;
    }

    @Override
    public Map<RoomEntityStatus, String> getStatuses() {
        return this.statuses;
    }

    @Override
    public void addStatus(RoomEntityStatus key, String value) {
        if (this.statuses.containsKey(key)) {
            this.statuses.replace(key, value);
        } else {
            this.statuses.put(key, value);
        }
    }

    @Override
    public void removeStatus(RoomEntityStatus status) {
        if (!this.statuses.containsKey(status)) {
            return;
        }

        this.statuses.remove(status);
    }

    @Override
    public boolean hasStatus(RoomEntityStatus key) {
        return this.statuses.containsKey(key);
    }

    @Override
    public void markNeedsUpdate() {
        this.needsUpdate = true;
    }

    public void setSendUpdateMessage(boolean sendUpdateMessage) {
        this.sendUpdateMessage = sendUpdateMessage;
    }

    public void markNeedsUpdate(boolean sendMessage) {
        this.needsUpdate = true;
        this.sendUpdateMessage = sendMessage;
    }

    public void markUpdateComplete() {
        this.needsUpdate = false;
    }

    @Override
    public boolean needsUpdate() {
        return this.needsUpdate;
    }

    public boolean isMoonwalking() {
        return this.isMoonwalking;
    }

    public void setIsMoonwalking(boolean isMoonwalking) {
        this.isMoonwalking = isMoonwalking;
    }

    @Override
    public int getIdleTime() {
        return this.idleTime;
    }

    public boolean isIdle() {
        return this.idleTime >= 600;
    }

    @Override
    public boolean isIdleAndIncrement() {
        this.idleTime++;

        if (this.idleTime >= 600) {
            if (!this.isIdle) {
                this.isIdle = true;
                this.getRoom().getEntities().broadcastMessage(new IdleStatusMessageComposer((PlayerEntity) this, true));
            }
            return true;
        }

        return false;
    }

    @Override
    public void resetIdleTime() {
        this.idleTime = 0;
    }

    @Override
    public void setIdle() {
        this.idleTime = 600;
    }

    public boolean handItemNeedsRemove() {
        if (this.handItemTimer == -999)
            return false;

        this.handItemTimer--;

        return this.handItemTimer <= 0;
    }

    public void unIdle() {
        final boolean sendUpdate = this.isIdle;
        this.isIdle = false;
        this.resetIdleTime();

        if (this instanceof BotEntity) {
            return;
        }

        if (sendUpdate) {
            this.getRoom().getEntities().broadcastMessage(new IdleStatusMessageComposer((PlayerEntity) this, false));
        }
    }

    @Override
    public int getSignTime() {
        return this.signTime;
    }

    @Override
    public void markDisplayingSign() {
        this.signTime = 6;
    }

    @Override
    public boolean isDisplayingSign() {
        this.signTime--;

        if (this.signTime <= 0) {
            if (this.signTime < 0) {
                this.signTime = 0;
            }

            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getDanceId() {
        return this.danceId;
    }

    @Override
    public void setDanceId(int danceId) {
        this.danceId = danceId;
    }

    @Override
    public PlayerEffect getCurrentEffect() {
        return this.effect;
    }

    @Override
    public int getHandItem() {
        return this.handItem;
    }

    @Override
    public void carryItem(int id) {
        this.carryItem(id, 240);
    }

    public void carryItem(int id, int timer) {
        this.handItem = id;
        this.handItemTimer = timer;

        this.getRoom().getEntities().broadcastMessage(new HandItemMessageComposer(this.getId(), handItem));
    }

    @Override
    public void carryItem(int id, boolean timer) {
        if (timer) {
            this.carryItem(id);
            return;
        }

        this.handItem = id;
        this.handItemTimer = -999;

        this.getRoom().getEntities().broadcastMessage(new HandItemMessageComposer(this.getId(), handItem));
    }

    @Override
    public int getHandItemTimer() {
        return this.handItemTimer;
    }

    @Override
    public void setHandItemTimer(int time) {
        this.handItemTimer = time;
    }

    @Override
    public void applyEffect(PlayerEffect effect) {
        if (effect == null) {
            if (this.teamEffect != null && this.effect != null) {
                this.applyEffect(teamEffect);
                return;
            }

            this.getRoom().getEntities().broadcastMessage(new ApplyEffectMessageComposer(this.getId(), 0));
        } else {

            PlayerEntity playerEntity = (PlayerEntity) this;
            int effectId = effect.getEffectId();

            if (playerEntity.getPlayer().getSettings().hasPersonalStaff() && effectId <= 0) {

                List<Map.Entry<Integer, Integer>> rankPermList = new ArrayList<>(PermissionsDao.getEffects().entrySet());
                rankPermList.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

                for (Map.Entry<Integer, Integer> entry : rankPermList) {

                    if (playerEntity.getPlayer().getPermissions().getRank().getId() < entry.getValue())
                        continue;

                    if (playerEntity.getPlayer().getSettings().hasPersonalStaff()) {
                        playerEntity.getPlayer().getEntity().applyEffect(new PlayerEffect(entry.getKey()));
                    } else
                        playerEntity.getPlayer().getEntity().applyEffect(new PlayerEffect(0));

                    break;
                }
            } else {
                final Integer minimumRank = PermissionsManager.getInstance().getEffects().get(effect.getEffectId());
                if (minimumRank != null && playerEntity.getPlayer().getData().getRank() < minimumRank) {
                    effectId = 10;
                }
                this.getRoom().getEntities().broadcastMessage(new ApplyEffectMessageComposer(this.getId(), effectId));
            }

        }

        if (effect != null && effect.expires()) {
            this.lastEffect = this.effect;
        }

        this.effect = effect;
    }

    public void applyTeamEffect(PlayerEffect effect) {
        this.teamEffect = effect;

        this.applyEffect(effect);
    }

    public boolean isOverriden() {
        return this.overriden;
    }

    public void setOverriden(boolean overriden) {
        this.overriden = overriden;
    }

    public abstract boolean joinRoom(Room room, String password);

    protected abstract void finalizeJoinRoom();

    public abstract void leaveRoom(boolean isOffline, boolean isKick, boolean toHotelView);

    public abstract boolean onChat(String message);

    public abstract boolean onRoomDispose();

    public boolean isVisible() {
        return isVisible;
    }

    public void updateVisibility(boolean isVisible) {
        if (isVisible && !this.isVisible) {
            this.getRoom().getEntities().broadcastMessage(new AvatarsMessageComposer(this));
        } else {
            this.getRoom().getEntities().broadcastMessage(new LeaveRoomMessageComposer(this.getId()));
        }

        this.isVisible = isVisible;
    }

    public void refresh() {
        this.updateVisibility(false);
        this.updateVisibility(true);
        this.markNeedsUpdate();
    }

    public void cancelWalk() {
        this.setWalkCancelled(true);
        this.markNeedsUpdate();
    }

    public void teleportToItem(RoomItemFloor itemFloor) {
        this.teleportToObject(itemFloor);
    }

    public void teleportToEntity(RoomEntity entity) {
        this.teleportToObject(entity);
    }

    public void teleportToObject(RoomObject roomObject) {
        this.applyEffect(new PlayerEffect(4, 5));

        final RoomTile tile = this.getRoom().getMapping().getTile(this.getPosition());
        this.warpedTile = tile;

        //System.out.println("entities on current tile: " + tile.getEntities().size());

        final Position position = roomObject.getPosition();
        position.setZ(roomObject.getTile().getWalkHeight());

        this.cancelWalk();
        this.warp(roomObject.getPosition());
    }

    public void warp(Position position, boolean cancelNextUpdate) {
        if (cancelNextUpdate) {
            this.cancelNextUpdate();
        } else {
            this.updatePhase = 1;
        }

        this.needsForcedUpdate = true;

        this.isWarped = true;
        final RoomTile tile = this.getRoom().getMapping().getTile(position);

        this.updateAndSetPosition(position);
        this.markNeedsUpdate();

        if (tile != null) {
            this.addToTile(tile);

            if (tile.getTopItemInstance() != null) {
                if (tile.getTopItemInstance() instanceof SeatFloorItem)
                    ((SeatFloorItem) tile.getTopItemInstance()).onEntityStepOn(this, false);
                else
                    tile.getTopItemInstance().onEntityStepOn(this);
            }
        }

    }

    @Override
    public void warp(Position position) {
//        if (this.needsForcedUpdate) return;

        this.warping = true;
        this.warp(position, true);
    }

    @Override
    public void warpImmediately(Position position) {
        this.setPosition(position);
        this.getRoom().getEntities().broadcastMessage(new AvatarUpdateMessageComposer(this));
    }

    public boolean needsUpdateCancel() {
        if (this.cancelNextUpdate) {
            this.cancelNextUpdate = false;
            return true;
        } else {
            return false;
        }
    }

    public void cancelNextUpdate() {
        this.cancelNextUpdate = true;
    }

    public boolean isDoorbellAnswered() {
        return this.doorbellAnswered;
    }

    public void setDoorbellAnswered(boolean b) {
        this.doorbellAnswered = b;
    }

    public PlayerEffect getLastEffect() {
        return lastEffect;
    }

    public void setLastEffect(PlayerEffect lastEffect) {
        this.lastEffect = lastEffect;
    }

    public boolean isWalkCancelled() {
        return walkCancelled;
    }

    public void setWalkCancelled(boolean walkCancelled) {
        this.walkCancelled = walkCancelled;
    }

    public RoomEntity getMountedEntity() {
        if (this.mountedEntity == null) return null;

        if (this.getRoom().getEntities().getEntity(this.mountedEntity.getId()) == null) {
            return null;
        }

        return mountedEntity;
    }

    public void setMountedEntity(RoomEntity mountedEntity) {
        this.mountedEntity = mountedEntity;
    }

    public boolean hasMount() {
        return hasMount;
    }

    public void setHasMount(boolean hasMount) {
        this.hasMount = hasMount;
    }

    @Override
    public void kick() {
        this.leaveRoom(false, true, true);
    }

    public boolean canWalk() {
        return canWalk;
    }

    public void setCanWalk(boolean canWalk) {
        this.canWalk = canWalk;
    }

    @Override
    public boolean equals(Object entity) {
        if (entity instanceof RoomEntity) {
            return ((RoomEntity) entity).getId() == this.getId();
        }

        return false;
    }

    public boolean isRoomMuted() {
        return isRoomMuted;
    }

    public void setRoomMuted(boolean isRoomMuted) {
        this.isRoomMuted = isRoomMuted;
    }

    @Override
    public long getJoinTime() {
        return joinTime;
    }

    public long getPrivateChatItemId() {
        return privateChatItemId;
    }

    public void setPrivateChatItemId(long privateChatItemId) {
        this.privateChatItemId = privateChatItemId;
    }

    public BotAI getAI() {
        return null;
    }

    public Set<RoomEntity> getFollowingEntities() {
        return followingEntities;
    }

    public Position getPendingWalk() {
        return pendingWalk;
    }

    public void setPendingWalk(Position pendingWalk) {
        this.pendingWalk = pendingWalk;
    }

    public void toggleFastWalk() {
        this.fastWalkEnabled = !this.fastWalkEnabled;
    }

    public boolean isFastWalkEnabled() {
        return this.fastWalkEnabled;
    }

    public void setFastWalkEnabled(boolean fastWalkEnabled) {
        this.fastWalkEnabled = fastWalkEnabled;
    }

    public boolean isWarped() {
        return isWarped;
    }

    public void setWarped(boolean warped) {
        isWarped = warped;
    }

    public int getPreviousSteps() {
        return previousSteps;
    }

    public void incrementPreviousSteps() {
        this.previousSteps++;
    }

    public boolean sendUpdateMessage() {
        return sendUpdateMessage;
    }

    public boolean isWarping() {
        return warping;
    }

    public void setWarping(boolean warping) {
        this.warping = warping;
    }

    public RoomTile getWarpedTile() {
        return warpedTile;
    }

    public void setWarpedTile(RoomTile warpedTile) {
        this.warpedTile = warpedTile;
    }

    public void removeFromTile(RoomTile tile) {
        tile.getEntities().remove(this);
        this.tiles.remove(tile);
    }

    public void addToTile(RoomTile tile) {
        if(this.tiles.size() != 0) {
            for(RoomTile oldTile : this.tiles) {
                oldTile.getEntities().remove(this);
            }

            this.tiles.clear();
        }

        tile.getEntities().add(this);
        this.tiles.add(tile);
    }

    public Set<RoomTile> getTiles() {
        return tiles;
    }
}
