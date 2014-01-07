package com.cometsrv.game.rooms.types.components;

import com.cometsrv.game.players.types.Player;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.entities.RoomEntityType;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomModel;
import com.cometsrv.network.messages.types.Composer;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import sun.net.www.content.text.Generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityComponent {
    private static Logger log = Logger.getLogger(EntityComponent.class.getName());

    private Room room;

    private AtomicInteger entityIdGenerator = new AtomicInteger();

    private Map<Integer, GenericEntity> entities = new FastMap<Integer, GenericEntity>().shared();
    private Map<Integer, Integer> playerEntityToPlayerId = new FastMap<Integer, Integer>().shared();

    private List<GenericEntity>[][] entityGrid;

    public EntityComponent(Room room, RoomModel model) {
        this.room = room;

        this.entityGrid = new ArrayList[model.getSizeX()][model.getSizeY()];
    }

    public List<GenericEntity> getEntitiesAt(int x, int y) {
        return this.entityGrid[x][y] != null ? this.entityGrid[x][y] : new ArrayList<GenericEntity>();
    }

    public void replaceEntityGrid(List<GenericEntity>[][] entityGrid) {
        this.entityGrid = entityGrid;
    }

    public void updateEntityGrid(GenericEntity entity, int prevX, int prevY, int newX, int newY) {
        // Synchronize access because the grid is not thread safe
        synchronized (this.entityGrid) {
            this.entityGrid[prevX][prevY].remove(entity);

            if (this.entityGrid[newX][newY] == null) {
                this.entityGrid[newX][newY] = new ArrayList<GenericEntity>();
            }

            this.entityGrid[newX][newY].add(entity);
        }
    }

    public boolean isSquareAvailable(int x, int y) {
        return this.entityGrid[x][y] == null || this.entityGrid[x][y].isEmpty();
    }

    public PlayerEntity createEntity(Player player) {
        Position3D startPosition = new Position3D(this.getRoom().getModel().getDoorX(), this.getRoom().getModel().getDoorY(), this.getRoom().getModel().getDoorZ());
        int doorRotation = this.getRoom().getModel().getDoorRotation();

        PlayerEntity entity = new PlayerEntity(player, this.getFreeId(), startPosition, doorRotation, doorRotation, this.getRoom());
        this.addEntity(entity);

        return entity;
    }

    protected void addEntity(GenericEntity entity) {
        // Handle adding players
        if (entity.getEntityType() == RoomEntityType.PLAYER) {
            PlayerEntity playerEntity = (PlayerEntity) entity;

            this.playerEntityToPlayerId.put(playerEntity.getVirtualId(), playerEntity.getPlayerId());
            this.entities.put(playerEntity.getVirtualId(), playerEntity);
        } else {
            // Handle all other entity types which just rely on a virtual id
            this.entities.put(entity.getVirtualId(), entity);
        }
    }

    public void removeEntity(GenericEntity entity) {
        // Handle removing player entities specifics
        if (entity.getEntityType() == RoomEntityType.PLAYER) {
            PlayerEntity playerEntity = (PlayerEntity) entity;

            this.playerEntityToPlayerId.remove(playerEntity.getPlayerId());
        }

        // Remove entity by the virtual id
        this.entities.remove(entity.getVirtualId());
    }

    public void broadcastMessage(Composer msg) {
        for (GenericEntity entity : this.entities.values()) {
            // We only broadcast to player entities
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                PlayerEntity playerEntity = (PlayerEntity) entity;

                playerEntity.getPlayer().getSession().send(msg);
            }
        }
    }

    public GenericEntity getEntity(int id) {
        return this.entities.get(id);
    }

    public PlayerEntity tryGetPlayerEntity(int id) throws Exception {
        GenericEntity entity = this.entities.get(id);

        if (entity.getEntityType() != RoomEntityType.PLAYER) {
            throw new Exception("This entity is not a player.");
        }

        return (PlayerEntity) entity;
    }

    protected int getFreeId() {
        return this.entityIdGenerator.incrementAndGet();
    }

    public int count() {
        return this.entities.size();
    }

    private Room getRoom() {
        return this.room;
    }

    public void dispose() {
        for(GenericEntity entity : entities.values()) {
            entity.leaveRoom();
        }

        entities.clear();
    }
}
