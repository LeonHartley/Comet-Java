package com.cometproject.server.game.rooms.objects;

import com.cometproject.api.game.bots.BotType;
import com.cometproject.api.game.rooms.objects.IRoomObject;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.api.game.utilities.Positionable;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.utilities.comporators.PositionComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class RoomObject implements IRoomObject, Positionable {
    /**
     * The room where this object resides
     */
    private Room room;

    /**
     * The position on the grid this object resides
     */
    private Position position;

    /**
     * Create the room object instance
     *
     * @param position The position in the room where this object is
     * @param room     The room where this object is
     */
    public RoomObject(Position position, Room room) {
        this.position = position;
        this.room = room;
    }

    /**
     * Gets the tile instance from the room mapping
     *
     * @return the tile instance from the room mapping
     */
    public RoomTile getTile() {
        if (this.getPosition() == null) return null;

        return this.getRoom().getMapping().getTile(this.getPosition().getX(), this.getPosition().getY());
    }

    /**
     * Checks whether or not the object is at the door tile
     *
     * @return Is the object on the door tile?
     */
    public boolean isAtDoor() {
        return this.position.getX() == this.getRoom().getModel().getDoorX() && this.position.getY() == this.getRoom().getModel().getDoorY();
    }

    /**
     * Gets the closest player entity
     *
     * @return The closest player entity | null if one couldn't be found
     */
    public PlayerEntity nearestPlayerEntity() {
        PositionComparator positionComporator = new PositionComparator(this);

        List<PlayerEntity> nearestEntities = this.getRoom().getEntities().getPlayerEntities();

        Collections.sort(nearestEntities, positionComporator);

        for (PlayerEntity playerEntity : nearestEntities) {
//            if (playerEntity.getTile().isReachable(this)) {
            return playerEntity;
//            }
        }

        return null;
    }

    /**
     * Gets the closest bot entity
     *
     * @param type The type of bot we want to find
     * @return Closest bot entity |
     */
    public BotEntity nearestBotEntity(BotType type) {
        PositionComparator positionComparator = new PositionComparator(this);

        List<BotEntity> bots = new ArrayList<>();
        List<BotEntity> nearestEntities = this.getRoom().getEntities().getBotEntities();

        if (type == null) {
            bots.addAll(nearestEntities);
        } else {
            for (BotEntity botEntity : nearestEntities) {
                if (botEntity.getData().getBotType() == type) {
                    bots.add(botEntity);
                }
            }
        }

        Collections.sort(bots, positionComparator);

        for (BotEntity botEntity : bots) {
            if (this.getPosition().distanceTo(botEntity.getPosition()) < 4) {
                return botEntity;
            }
        }

        if (bots.size() >= 1) {
            return bots.get(0); // no bots found, find the closest one.
        }

        return null;
    }

    /**
     * Gets the closest bot entity
     *
     * @return The closest bot entity
     */
    public BotEntity nearestBotEntity() {
        return nearestBotEntity(null);
    }

    /**
     * Get the room where this object is
     *
     * @return The room instance
     */
    public Room getRoom() {
        return this.room;
    }

    /**
     * Get the position in which this object is on the grid
     *
     * @return The position instance
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Set the position to a new position
     *
     * @param newPosition The position to replace the instance one with
     */
    public void setPosition(Position newPosition) {
        if (newPosition == null) return;

        this.position = newPosition.copy();
    }
}
