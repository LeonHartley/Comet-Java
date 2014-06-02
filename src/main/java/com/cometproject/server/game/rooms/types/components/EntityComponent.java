package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.RoomEntityType;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.types.floor.TeleporterFloorItem;
import com.cometproject.server.game.rooms.models.RoomModel;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.settings.RoomRatingMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import io.netty.util.ReferenceCountUtil;
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

    private Map<Integer, Integer> playerIdToEntity = new FastMap<>();
    private Map<Integer, Integer> botIdToEntity = new FastMap<>();
    private Map<Integer, Integer> petIdToEntity = new FastMap<>();

    private List<GenericEntity>[][] entityGrid;

    public EntityComponent(Room room, RoomModel model) {
        this.room = room;

        this.entityGrid = new ArrayList[model.getSizeX()][model.getSizeY()];
    }

    public List<GenericEntity> getEntitiesAt(int x, int y) {
        if(x < entityGrid.length) {
            if(y < entityGrid[x].length) {
                return this.entityGrid[x][y] != null ? this.entityGrid[x][y] : new ArrayList<GenericEntity>();
            }
        }

        return new ArrayList<>();
    }

    public void replaceEntityGrid(List<GenericEntity>[][] entityGrid) {
        this.entityGrid = entityGrid;
    }

    public boolean isSquareAvailable(int x, int y) {
        return this.entityGrid[x][y] == null || this.entityGrid[x][y].isEmpty();
    }

    public PlayerEntity createEntity(Player player) {
        Position3D startPosition = new Position3D(this.getRoom().getModel().getDoorX(), this.getRoom().getModel().getDoorY(), this.getRoom().getModel().getDoorZ());

        if (player.isTeleporting()) {
            RoomItemFloor item = this.room.getItems().getFloorItem(player.getTeleportId());

            if (item != null) {
                startPosition = new Position3D(item.getX(), item.getY(), item.getHeight());
            }
        }

        int doorRotation = this.getRoom().getModel().getDoorRotation();

        PlayerEntity entity = new PlayerEntity(player, this.getFreeId(), startPosition, doorRotation, doorRotation, this.getRoom());

        if (player.isTeleporting()) {
            RoomItemFloor flItem = this.room.getItems().getFloorItem(player.getTeleportId());

            if (flItem != null && flItem instanceof TeleporterFloorItem) {
                TeleporterFloorItem item = (TeleporterFloorItem) flItem;
                item.handleIncomingEntity(entity, null);
            }

            player.setTeleportId(0);
        }

        return entity;
    }

    public void addEntity(GenericEntity entity) {
        if(this.playerIdToEntity == null) {
            this.playerIdToEntity = new FastMap<>();
        }

        if(this.botIdToEntity == null) {
            this.botIdToEntity = new FastMap<>();
        }

        if(this.petIdToEntity == null) {
            this.petIdToEntity = new FastMap<>();
        }

        if (entity.getEntityType() == RoomEntityType.PLAYER) {
            PlayerEntity playerEntity = (PlayerEntity) entity;

            this.playerIdToEntity.put(playerEntity.getPlayerId(), playerEntity.getVirtualId());
        } else if (entity.getEntityType() == RoomEntityType.BOT) {
            BotEntity botEntity = (BotEntity) entity;

            this.botIdToEntity.put(botEntity.getBotId(), botEntity.getVirtualId());
        } else if (entity.getEntityType() == RoomEntityType.PET) {
            PetEntity petEntity = (PetEntity) entity;

            this.petIdToEntity.put(petEntity.getData().getId(), petEntity.getVirtualId());
        }

        this.entities.put(entity.getVirtualId(), entity);
    }

    public void removeEntity(GenericEntity entity) {
        // Handle removing entity specifics
        if (entity.getEntityType() == RoomEntityType.PLAYER) {
            PlayerEntity playerEntity = (PlayerEntity) entity;

            this.playerIdToEntity.remove(playerEntity.getPlayerId());
        } else if (entity.getEntityType() == RoomEntityType.BOT) {
            BotEntity botEntity = (BotEntity) entity;

            this.botIdToEntity.remove(botEntity.getBotId());
        } else if (entity.getEntityType() == RoomEntityType.PET) {
            PetEntity petEntity = (PetEntity) entity;

            this.petIdToEntity.remove(petEntity.getData().getId());
        }

        this.entities.remove(entity.getVirtualId());
    }

    public void broadcastMessage(Composer msg, boolean usersWithRightsOnly) {
        try {
            for (GenericEntity entity : this.entities.values()) {
                if (entity == null) {
                    continue;
                }

                if (entity.getEntityType() == RoomEntityType.PLAYER) {
                    PlayerEntity playerEntity = (PlayerEntity) entity;

                    if (usersWithRightsOnly && !this.room.getRights().hasRights(playerEntity.getPlayerId()))
                        continue;

                    playerEntity.getPlayer().getSession().getChannel().writeAndFlush(msg.duplicate().retain());

                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    public void broadcastMessage(Composer msg) {
        broadcastMessage(msg, false);
    }

    public GenericEntity getEntity(int id) {
        return this.entities.get(id);
    }

    public PlayerEntity getEntityByPlayerId(int id) {
        if (!this.playerIdToEntity.containsKey(id)) {
            return null;
        }

        int entityId = this.playerIdToEntity.get(id);
        GenericEntity genericEntity = this.entities.get(entityId);

        if (genericEntity == null || genericEntity.getEntityType() != RoomEntityType.PLAYER) {
            return null;
        }

        return (PlayerEntity) genericEntity;
    }

    public GenericEntity getEntityByName(String name, RoomEntityType type) {
        for(GenericEntity entity : this.getEntitiesCollection().values()) {
            if(entity.getUsername().equals(name) && entity.getEntityType() == type) {
                return entity;
            }
        }

        return null;
    }

    public BotEntity getEntityByBotId(int id) {
        if (!this.botIdToEntity.containsKey(id)) {
            return null;
        }

        int entityId = this.botIdToEntity.get(id);
        GenericEntity genericEntity = this.entities.get(entityId);

        if (genericEntity == null || genericEntity.getEntityType() != RoomEntityType.BOT) {
            return null;
        }

        return (BotEntity) genericEntity;
    }

    public PetEntity getEntityByPetId(int id) {
        if (!this.petIdToEntity.containsKey(id)) {
            return null;
        }

        int entityId = this.petIdToEntity.get(id);
        GenericEntity genericEntity = this.entities.get(entityId);

        if (genericEntity == null || genericEntity.getEntityType() != RoomEntityType.PET) {
            return null;
        }

        return (PetEntity) genericEntity;
    }

    public List<BotEntity> getBotEntities() {
        List<BotEntity> entities = new ArrayList<>();

        for (GenericEntity entity : this.entities.values()) {
            if (entity.getEntityType() == RoomEntityType.BOT) {
                entities.add((BotEntity) entity);
            }
        }

        return entities;
    }

    public List<PetEntity> getPetEntities() {
        List<PetEntity> entities = new ArrayList<>();

        for (GenericEntity entity : this.entities.values()) {
            if (entity.getEntityType() == RoomEntityType.PET) {
                entities.add((PetEntity) entity);
            }
        }

        return entities;
    }

    public List<PlayerEntity> getPlayerEntities() {
        List<PlayerEntity> entities = new ArrayList<>();

        if(this.entities == null || this.entities.size() < 1) {
            return entities;
        }

        for (GenericEntity entity : this.entities.values()) {
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                entities.add((PlayerEntity) entity);
            }
        }

        return entities;
    }

    public void refreshScore() {
        for(PlayerEntity entity : getPlayerEntities()) {
            entity.getPlayer().getSession().send(RoomRatingMessageComposer.compose(room.getData().getScore(), entity.canRateRoom()));
        }
    }

    protected int getFreeId() {
        return this.entityIdGenerator.incrementAndGet();
    }

    public int count() {
        return this.entities.size();
    }

    public int playerCount() {
        return this.playerIdToEntity == null ? 0 : this.playerIdToEntity.size();
    }

    public Map<Integer, GenericEntity> getEntitiesCollection() {
        return this.entities;
    }

    private Room getRoom() {
        return this.room;
    }

    public void dispose() {
        for (GenericEntity entity : entities.values()) {
            if (entity.getEntityType() == RoomEntityType.PET) {
                ((PetEntity) entity).leaveRoom(true); // save pet data
            } else {
                entity.leaveRoom(false, false, true);
            }
        }

        playerIdToEntity.clear();
        petIdToEntity.clear();
        botIdToEntity.clear();

        playerIdToEntity = null;
        petIdToEntity = null;
        botIdToEntity = null;

        entities = null;
    }
}
