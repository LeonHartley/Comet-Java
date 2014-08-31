package com.cometproject.server.game.rooms.entities;

import com.cometproject.server.game.rooms.avatars.effects.UserEffect;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.Pathfinder;
import com.cometproject.server.game.rooms.avatars.pathfinding.Square;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.outgoing.room.avatar.*;
import javolution.util.FastMap;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public abstract class GenericEntity implements AvatarEntity {
    private int id;

    private RoomEntityType entityType;

    private Position3D position;
    private Position3D walkingGoal;
    private Position3D positionToSet;

    private int bodyRotation;
    private int headRotation;

    private int roomId;
    private WeakReference<Room> room;

    private List<Square> processingPath;
    private List<Square> walkingPath;

    private Square futureSquare;

    private Pathfinder pathfinder;
    private int stepsToGoal;

    private int idleTime;
    private int signTime;

    private int danceId;
    private UserEffect effect;

    private int handItem;
    private int handItemTimer;

    private boolean markedNeedsUpdate;
    private boolean isMoonwalking;
    private boolean overriden;
    private boolean isVisible;

    private boolean doorbellAnswered;

    private Map<String, String> statusses = new FastMap<>();

    public GenericEntity(int identifier, Position3D startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        this.id = identifier;

        // Set the entity type
        if (this instanceof PlayerEntity) {
            this.entityType = RoomEntityType.PLAYER;
        } else if (this instanceof BotEntity) {
            this.entityType = RoomEntityType.BOT;
        } else if (this instanceof PetEntity) {
            this.entityType = RoomEntityType.PET;
        }

        this.position = startPosition;

        this.bodyRotation = startBodyRotation;
        this.headRotation = startHeadRotation;

        this.roomId = roomInstance.getId();
        this.room = new WeakReference<>(roomInstance);

        this.idleTime = 0;
        this.signTime = 0;
        this.handItem = 0;
        this.handItemTimer = 0;

        this.danceId = 0;

        this.markedNeedsUpdate = false;
        this.isMoonwalking = false;
        this.overriden = false;
        this.isVisible = true;

        this.doorbellAnswered = false;

        this.stepsToGoal = 0;
    }

    @Override
    public int getVirtualId() {
        return this.id;
    }

    public RoomEntityType getEntityType() {
        return this.entityType;
    }

    @Override
    public Position3D getPosition() {
        return this.position;
    }

    @Override
    public Position3D getWalkingGoal() {
        if (this.walkingGoal == null) {
            return this.position;
        } else {
            return this.walkingGoal;
        }
    }

    @Override
    public void setWalkingGoal(int x, int y) {
        if (this.walkingGoal == null) {
            this.walkingGoal = new Position3D(x, y, 0.0);
        } else {
            this.walkingGoal.setX(x);
            this.walkingGoal.setY(y);
        }
    }

    public int getStepsToGoal() {
        return this.stepsToGoal;
    }

    @Override
    public void moveTo(int x, int y) {
        // TODO: Redirection grid here for beds

        if (this.getPositionToSet() != null) {
            this.getRoom().getEntities().getEntitiesAt(this.getPosition().getX(), this.getPosition().getY()).remove(this);
            this.setPosition(this.getPositionToSet());
        }

        // Set the goal we are wanting to achieve
        this.setWalkingGoal(x, y);

        // Create a walking path
        List<Square> path = this.getPathfinder().makePath();

        // Check returned path to see if it calculated one
        if (path == null || path.size() == 0) {
            // Reset the goal and return as no path was found
            this.setWalkingGoal(this.getPosition().getX(), this.getPosition().getY());
            return;
        }

        this.stepsToGoal = path.size();

        // UnIdle the user and set the path (if the path has nodes it will mean the user is walking)
        this.unIdle();
        this.setWalkingPath(path);
    }

    @Override
    public void setPosition(Position3D pos) {
        if(pos == null)
            return;

        if (this.position == null) {
            this.position = pos;
        } else {
            this.position.setX(pos.getX());
            this.position.setY(pos.getY());
            this.position.setZ(pos.getZ());
        }
    }

    @Override
    public Position3D getPositionToSet() {
        return this.positionToSet;
    }

    @Override
    public void updateAndSetPosition(Position3D pos) {
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
    public Room getRoom() {
        return this.room.get();
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
    public Pathfinder getPathfinder() {
        if (this.pathfinder == null) {
            this.pathfinder = new Pathfinder(this);
        }

        return this.pathfinder;
    }

    @Override
    public Map<String, String> getStatuses() {
        return this.statusses;
    }

    @Override
    public void addStatus(String key, String value) {
        if (this.statusses.containsKey(key)) {
            return;
        }

        this.statusses.put(key, value);
    }

    @Override
    public void removeStatus(String key) {
        if (!this.statusses.containsKey(key)) {
            return;
        }

        this.statusses.remove(key);
    }

    @Override
    public boolean hasStatus(String key) {
        return this.statusses.containsKey(key);
    }

    @Override
    public void markNeedsUpdate() {
        this.markedNeedsUpdate = true;
    }

    public void markNeedsUpdateComplete() {
        this.markedNeedsUpdate = false;
    }

    @Override
    public boolean needsUpdate() {
        return this.markedNeedsUpdate;
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

    @Override
    public boolean isIdleAndIncrement() {
        this.idleTime++;

        if (this.idleTime >= 600) {
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
        this.handItemTimer--;

        return this.handItemTimer <= 0;

    }

    public void unIdle() {
        this.resetIdleTime();
        this.getRoom().getEntities().broadcastMessage(IdleStatusMessageComposer.compose(this.getVirtualId(), false));
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
    public UserEffect getCurrentEffect() {
        return this.effect;
    }

    @Override
    public int getHandItem() {
        return this.handItem;
    }

    @Override
    public void carryItem(int id) {
        this.handItem = id;

        this.handItemTimer = 240;
        this.getRoom().getEntities().broadcastMessage(HandItemMessageComposer.compose(this.getVirtualId(), handItem));
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
    public void applyEffect(UserEffect effect) {
        if (effect == null) {
            this.getRoom().getEntities().broadcastMessage(ApplyEffectMessageComposer.compose(this.id, 0));
        } else {
            this.getRoom().getEntities().broadcastMessage(ApplyEffectMessageComposer.compose(this.id, effect.getEffectId()));
        }

        this.effect = effect;
    }

    public int distance(GenericEntity entity) {
        int avatarX = entity.getPosition().getX();
        int avatarY = entity.getPosition().getY();

        return DistanceCalculator.calculate(avatarX, avatarY, this.getPosition().getX(), this.getPosition().getY());
    }

    public boolean touching(GenericEntity entity) {
        int avatarX = entity.getPosition().getX();
        int avatarY = entity.getPosition().getY();

        return DistanceCalculator.tilesTouching(avatarX, avatarY, this.getPosition().getX(), this.getPosition().getY());
    }

    public Position3D squareInfront() {
        Position3D pos = new Position3D(0, 0, 0);

        int posX = this.getPosition().getX();
        int posY = this.getPosition().getY();

        if (this.getBodyRotation() == 0) {
            posY--;
        } else if (this.getBodyRotation() == 2) {
            posX++;
        } else if (this.getBodyRotation() == 4) {
            posY++;
        } else if (this.getBodyRotation() == 6) {
            posX--;
        }

        pos.setX(posX);
        pos.setY(posY);

        return pos;
    }

    public Position3D squareBehind() {
        Position3D pos = new Position3D(0, 0, 0);

        int posX = this.getPosition().getX();
        int posY = this.getPosition().getY();

        if (this.getBodyRotation() == 0) {
            posY++;
        } else if (this.getBodyRotation() == 2) {
            posX--;
        } else if (this.getBodyRotation() == 4) {
            posY--;
        } else if (this.getBodyRotation() == 6) {
            posX++;
        }

        pos.setX(posX);
        pos.setY(posY);

        return pos;
    }

    public boolean isOverriden() {
        return this.overriden;
    }

    public void setOverriden(boolean overriden) {
        this.overriden = overriden;
    }

    public abstract void joinRoom(Room room, String password);

    protected abstract void finalizeJoinRoom();

    public abstract void leaveRoom(boolean isOffline, boolean isKick, boolean toHotelView);

    protected abstract void finalizeLeaveRoom();

    public abstract boolean onChat(String message);

    public abstract boolean onRoomDispose();

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void updateVisibility(boolean isVisible) {
        if (isVisible && !this.isVisible) {
            this.getRoom().getEntities().broadcastMessage(AvatarsMessageComposer.compose(this));
        } else {
            this.getRoom().getEntities().broadcastMessage(LeaveRoomMessageComposer.compose(this.getVirtualId()));
        }

        this.isVisible = isVisible;
    }

    public boolean isDoorbellAnswered() {
        return this.doorbellAnswered;
    }

    public void setDoorbellAnswered(boolean b) {
        this.doorbellAnswered = b;
    }
}
