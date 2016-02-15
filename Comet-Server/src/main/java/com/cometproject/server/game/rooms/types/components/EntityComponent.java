package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.TeleporterFloorItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.RoomRatingMessageComposer;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class EntityComponent {

    private static Logger log = Logger.getLogger(EntityComponent.class.getName());

    private Room room;

    private AtomicInteger entityIdGenerator = new AtomicInteger();
    private final Map<Integer, RoomEntity> entities = new ConcurrentHashMap<>();

    private final Map<Integer, Integer> playerIdToEntity = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> botIdToEntity = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> petIdToEntity = new ConcurrentHashMap<>();

    public EntityComponent(Room room) {
        this.room = room;
    }

    public List<RoomEntity> getEntitiesAt(Position position) {
        RoomTile tile = this.getRoom().getMapping().getTile(position.getX(), position.getY());

        if (tile != null && tile.getEntities().size() >= 1) {
            return new ArrayList<>(tile.getEntities());
        }

        return new ArrayList<>();
    }

    public boolean positionHasEntity(Position position) {
        RoomTile tile = this.getRoom().getMapping().getTile(position.getX(), position.getY());

        if (tile != null) {
            if (tile.getEntities().size() != 0)
                return true;
        }

        return false;
    }

    public PlayerEntity createEntity(Player player) {
        Position startPosition = new Position(this.getRoom().getModel().getDoorX(), this.getRoom().getModel().getDoorY(), this.getRoom().getModel().getDoorZ());

        if (player.isTeleporting()) {
            RoomItemFloor item = this.room.getItems().getFloorItem(player.getTeleportId());

            if (item != null) {
                startPosition = new Position(item.getPosition().getX(), item.getPosition().getY(), item.getPosition().getZ());
            }
        }

        int doorRotation = this.getRoom().getModel().getDoorRotation();

        PlayerEntity entity = new PlayerEntity(player, this.getFreeId(), startPosition, doorRotation, doorRotation, this.getRoom());

        if (player.isTeleporting()) {
            RoomItemFloor flItem = this.room.getItems().getFloorItem(player.getTeleportId());

            if (flItem != null && (flItem instanceof TeleporterFloorItem)) {
                ((TeleporterFloorItem) flItem).handleIncomingEntity(entity, null);
            }
        }

        return entity;
    }

    public void addEntity(RoomEntity entity) {
        if (entity.getEntityType() == RoomEntityType.PLAYER) {
            PlayerEntity playerEntity = (PlayerEntity) entity;

            this.playerIdToEntity.put(playerEntity.getPlayerId(), playerEntity.getId());
        } else if (entity.getEntityType() == RoomEntityType.BOT) {
            BotEntity botEntity = (BotEntity) entity;

            this.botIdToEntity.put(botEntity.getBotId(), botEntity.getId());
        } else if (entity.getEntityType() == RoomEntityType.PET) {
            PetEntity petEntity = (PetEntity) entity;

            this.petIdToEntity.put(petEntity.getData().getId(), petEntity.getId());
        }

        this.entities.put(entity.getId(), entity);
    }

    public void removeEntity(RoomEntity entity) {
        final RoomTile tile = this.getRoom().getMapping().getTile(entity.getPosition());

        if (tile != null) {
            tile.getEntities().remove(entity);
        }

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

        this.entities.remove(entity.getId());
    }

    public void broadcastMessage(MessageComposer msg, boolean usersWithRightsOnly) {
        if (msg == null) return;

        for (RoomEntity entity : this.entities.values()) {
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                PlayerEntity playerEntity = (PlayerEntity) entity;

                if (playerEntity.getPlayer() == null)
                    continue;

                if (usersWithRightsOnly && !this.room.getRights().hasRights(playerEntity.getPlayerId()) && !playerEntity.getPlayer().getPermissions().getRank().roomFullControl()) {
                    continue;
                }

                playerEntity.getPlayer().getSession().send(msg);
            }
        }
    }

    public void broadcastChatMessage(MessageComposer msg, PlayerEntity sender) {
        for (RoomEntity entity : this.entities.values()) {
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                PlayerEntity playerEntity = (PlayerEntity) entity;

                if (playerEntity.getPlayer() == null)
                    continue;

                if (!playerEntity.getPlayer().ignores(sender.getPlayerId()))
                    playerEntity.getPlayer().getSession().send(msg);
            }
        }
    }

    public void broadcastMessage(MessageComposer msg) {
        broadcastMessage(msg, false);
    }

    public RoomEntity getEntity(int id) {
        return this.entities.get(id);
    }

    public PlayerEntity getEntityByPlayerId(int id) {
        if (!this.playerIdToEntity.containsKey(id)) {
            return null;
        }

        int entityId = this.playerIdToEntity.get(id);
        RoomEntity roomEntity = this.entities.get(entityId);

        if (roomEntity == null || roomEntity.getEntityType() != RoomEntityType.PLAYER) {
            return null;
        }

        return (PlayerEntity) roomEntity;
    }

    public RoomEntity getEntityByName(String name, RoomEntityType type) {
        for (RoomEntity entity : this.getAllEntities().values()) {
            if (entity.getUsername() == null) continue;

            if (entity.getUsername().equals(name) && entity.getEntityType() == type) {
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
        RoomEntity roomEntity = this.entities.get(entityId);

        if (roomEntity == null || roomEntity.getEntityType() != RoomEntityType.BOT) {
            return null;
        }

        return (BotEntity) roomEntity;
    }

    public PetEntity getEntityByPetId(int id) {
        if (!this.petIdToEntity.containsKey(id)) {
            return null;
        }

        int entityId = this.petIdToEntity.get(id);
        RoomEntity roomEntity = this.entities.get(entityId);

        if (roomEntity == null || roomEntity.getEntityType() != RoomEntityType.PET) {
            return null;
        }

        return (PetEntity) roomEntity;
    }

    public List<BotEntity> getBotEntities() {
        List<BotEntity> entities = new ArrayList<>();

        for (RoomEntity entity : this.entities.values()) {
            if (entity.getEntityType() == RoomEntityType.BOT) {
                entities.add((BotEntity) entity);
            }
        }

        return entities;
    }

    public List<PetEntity> getPetEntities() {
        List<PetEntity> entities = new ArrayList<>();

        for (RoomEntity entity : this.entities.values()) {
            if (entity.getEntityType() == RoomEntityType.PET) {
                entities.add((PetEntity) entity);
            }
        }

        return entities;
    }

    public List<PlayerEntity> getPlayerEntities() {
        List<PlayerEntity> entities = new ArrayList<>();

        if (this.entities == null || this.entities.size() < 1) {
            return entities;
        }

        for (RoomEntity entity : this.entities.values()) {
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                entities.add((PlayerEntity) entity);
            }
        }

        return entities;
    }

    public List<PlayerEntity> getWhisperSeers() {
        List<PlayerEntity> entities = new ArrayList<>();

        if (this.entities == null || this.entities.size() < 1) {
            return entities;
        }

        for (RoomEntity entity : this.entities.values()) {
            if (entity.getEntityType() == RoomEntityType.PLAYER) {
                if (((PlayerEntity) entity).getPlayer().getPermissions().getRank().roomSeeWhispers())
                    entities.add((PlayerEntity) entity);
            }
        }

        return entities;
    }

    public void refreshScore() {
        for (PlayerEntity entity : getPlayerEntities()) {
            entity.getPlayer().getSession().send(new RoomRatingMessageComposer(room.getData().getScore(), entity.canRateRoom()));
        }
    }

    protected int getFreeId() {
        return this.entityIdGenerator.incrementAndGet();
    }

    public int count() {
        int count = 0;

        for (RoomEntity entity : this.entities.values()) {
            if (entity.isVisible()) count++;
        }

        return count;
    }

    public int playerCount() {
        List<Integer> countedEntities = Lists.newArrayList();

        try {
            for (RoomEntity entity : this.entities.values()) {
                if (entity instanceof PlayerEntity && entity.isVisible()) {
                    if(!countedEntities.contains(((PlayerEntity) entity).getPlayerId())) {
                        countedEntities.add(((PlayerEntity) entity).getPlayerId());
                    }
                }
            }

            return countedEntities.size();
        } catch(Exception e) {
            return 0;
        } finally {
            countedEntities.clear();
        }
    }

    public int realPlayerCount() {
        return this.playerIdToEntity.size();
    }

    public Map<Integer, RoomEntity> getAllEntities() {
        return this.entities;
    }

    public Room getRoom() {
        return this.room;
    }

    public void dispose() {
        for (Map.Entry<Integer, RoomEntity> entity : this.entities.entrySet()) {
            entity.getValue().onRoomDispose();
        }

        this.entities.clear();

        this.playerIdToEntity.clear();
        this.botIdToEntity.clear();
        this.petIdToEntity.clear();
    }
}
