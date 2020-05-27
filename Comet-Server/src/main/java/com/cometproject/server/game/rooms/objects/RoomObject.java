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
import com.google.common.collect.Sets;
import org.apache.commons.collections4.SetUtils;

import java.util.*;


public abstract class RoomObject implements IRoomObject, Positionable {
    private final Room room;
    private Position position;

    public RoomObject(Position position, Room room) {
        this.position = position;
        this.room = room;
    }

    public RoomTile getTile() {
        if (this.getPosition() == null) return null;

        return this.getRoom().getMapping().getTile(this.getPosition().getX(), this.getPosition().getY());
    }

    public boolean isAtDoor() {
        return this.position.getX() == this.getRoom().getModel().getDoorX() && this.position.getY() == this.getRoom().getModel().getDoorY();
    }

    public PlayerEntity nearestPlayerEntity() {
        return nearestPlayerEntity(null);
    }

    public PlayerEntity nearestPlayerEntity(Set<Integer> excludingEntities) {
        PositionComparator positionComparator = new PositionComparator(this);
        List<PlayerEntity> nearestEntities = this.getRoom().getEntities().getPlayerEntities();

        nearestEntities.sort(positionComparator);

        for (PlayerEntity playerEntity : nearestEntities) {
            if (excludingEntities == null || !excludingEntities.contains(playerEntity.getId())) {
                return playerEntity;
            }
        }

        return null;
    }

    public BotEntity nearestBotEntity(BotType type) {
        PositionComparator positionComparator = new PositionComparator(this);

        List<BotEntity> bots = new ArrayList<>();
        List<BotEntity> nearestEntities = this.getRoom().getEntities().getBotEntities();

        if (nearestEntities.isEmpty()) {
            return null;
        }

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

    public Room getRoom() {
        return this.room;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position newPosition) {
        if (newPosition == null) return;

        this.position = newPosition.copy();
    }
}
