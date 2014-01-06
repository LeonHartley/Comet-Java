package com.cometsrv.game.rooms.entities;

import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.avatars.pathfinding.PathfinderNew;
import com.cometsrv.game.rooms.avatars.pathfinding.Square;
import com.cometsrv.game.rooms.entities.types.BotEntity;
import com.cometsrv.game.rooms.entities.types.PetEntity;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.types.Room;
import com.sun.java.swing.plaf.windows.resources.windows_es;
import javolution.util.FastMap;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public abstract class GenericEntity implements AvatarEntity {
    private int id;

    private RoomEntityType entityType;

    private Position3D position;
    private Position3D walkingGoal;

    private int roomId;
    private WeakReference<Room> room;

    private List<Square> walkingPath;
    private PathfinderNew pathfinder;

    private int idleTime;
    private int signTime;

    private Map<String, String> statusses = new FastMap<>();

    public GenericEntity(int identifier, Position3D startPosition, Room roomInstance) {
        this.id = identifier;

        // Set the entity type
        if (this instanceof PlayerEntity) {
            this.entityType = entityType.PLAYER;
        } else if (this instanceof BotEntity) {
            this.entityType = entityType.BOT;
        } else if (this instanceof PetEntity) {
            this.entityType = entityType.PET;
        }

        this.position = startPosition;

        this.roomId = roomInstance.getId();
        this.room = new WeakReference<>(roomInstance);

        this.idleTime = 0;
        this.signTime = 0;
    }

    @Override
    public int getId() {
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

    @Override
    public void setPosition(Position3D pos) {
        if (this.position == null) {
            this.position = pos;
        } else {
            this.position.setX(pos.getX());
            this.position.setY(pos.getY());
            this.position.setZ(pos.getZ());
        }
    }

    @Override
    public Room getRoom() {
        if (this.room.isEnqueued()) { return null; }
        return this.room.get();
    }

    @Override
    public List<Square> getWalkingPath() {
        return this.walkingPath;
    }

    @Override
    public void setWalkingPath(List<Square> path) {
        this.walkingPath = path;
    }

    @Override
    public boolean isWalking() {
        return (this.walkingPath != null) && (this.walkingPath.size() > 0);
    }

    @Override
    public PathfinderNew getPathfinder() {
        if (this.pathfinder == null) {
            this.pathfinder = new PathfinderNew(this);
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

    public abstract void joinRoom();
    protected abstract void finalizeJoinRoom();

    public abstract void leaveRoom();

    public abstract void onChat(String message);
}
