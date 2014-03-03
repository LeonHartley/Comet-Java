package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.RoomEntityType;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomModel;
import com.cometproject.server.network.messages.types.Composer;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

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
    private Map<Integer, Integer> botEntityToBotId = new FastMap<Integer, Integer>().shared();

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

    public void addEntity(GenericEntity entity) {
        // Handle adding players
        if (entity.getEntityType() == RoomEntityType.PLAYER) {
            PlayerEntity playerEntity = (PlayerEntity) entity;

            this.playerEntityToPlayerId.put(playerEntity.getVirtualId(), playerEntity.getPlayerId());
        } else if(entity.getEntityType() == RoomEntityType.BOT) {
            BotEntity botEntity = (BotEntity) entity;

            this.botEntityToBotId.put(botEntity.getVirtualId(), botEntity.getBotId());
        }

        this.entities.put(entity.getVirtualId(), entity);
    }

    public void removeEntity(GenericEntity entity) {
        // Handle removing entity specifics
        if (entity.getEntityType() == RoomEntityType.PLAYER) {
            PlayerEntity playerEntity = (PlayerEntity) entity;

            this.playerEntityToPlayerId.remove(playerEntity.getVirtualId());
            this.entities.remove(playerEntity.getVirtualId());
        } else if(entity.getEntityType() == RoomEntityType.BOT) {
            BotEntity botEntity = (BotEntity) entity;

            this.botEntityToBotId.remove(botEntity.getBotId());
            this.entities.remove(botEntity.getVirtualId());
        }
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

    public PlayerEntity tryGetPlayerEntityNullable(int id) {
        GenericEntity entity = this.entities.get(id);

        if (entity == null || entity.getEntityType() != RoomEntityType.PLAYER) {
            return null;

            // Instead of throwing an exception i will return null and add 'Nullable' to this method as a reminder to always check null!
            //throw new Exception("This entity is not a player.");
        }

        return (PlayerEntity) entity;
    }

    public PlayerEntity getEntityByPlayerId(int id) {
        if (!this.playerEntityToPlayerId.containsKey(id)) {
            return null;
        }

        int entityId = this.playerEntityToPlayerId.get(id);
        GenericEntity genericEntity = this.entities.get(entityId);

        if (genericEntity == null || genericEntity.getEntityType() != RoomEntityType.PLAYER) {
            return null;
        }

        return (PlayerEntity) genericEntity;
    }

    public BotEntity getEntityByBotId(int id) {
        if(!this.botEntityToBotId.containsKey(id)) {
            return null;
        }

        int entityId = this.botEntityToBotId.get(id);
        GenericEntity genericEntity = this.entities.get(entityId);

        if(genericEntity == null || genericEntity.getEntityType() != RoomEntityType.BOT) {
            return null;
        }

        return (BotEntity) genericEntity;
    }

    public List<BotEntity> getBotEntities() {
        List<BotEntity> entities = new FastList<>();

        for(GenericEntity entity : this.entities.values()) {
            if(entity.getEntityType() == RoomEntityType.BOT) {
                entities.add((BotEntity)entity);
            }
        }

        return entities;
    }

    protected int getFreeId() {
        return this.entityIdGenerator.incrementAndGet();
    }

    public int count() {
        return this.entities.size();
    }

    public int playerCount() {
        return this.playerEntityToPlayerId.size();
    }

    public Map<Integer, GenericEntity> getEntitiesCollection() {
        return this.entities;
    }

    @Deprecated
    public void broadcast(Composer msg) {
        this.broadcastMessage(msg);
    }

    private Room getRoom() {
        return this.room;
    }

    public void dispose() {
        for(GenericEntity entity : entities.values()) {
            entity.leaveRoom(false, false, true);
        }

        entities.clear();
    }
}
