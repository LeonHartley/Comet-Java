package com.cometsrv.game.rooms.types.components;

import com.cometsrv.game.players.types.Player;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.entities.RoomEntityType;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.types.Composer;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityComponent {
    private static Logger log = Logger.getLogger(EntityComponent.class.getName());

    private Room room;

    private AtomicInteger entityIdGenerator = new AtomicInteger();

    private Map<Integer, GenericEntity> entities = new FastMap<Integer, GenericEntity>().shared();
    private Map<Integer, Integer> playerEntityToPlayerId = new FastMap<Integer, Integer>().shared();

    public EntityComponent(Room room) {
        this.room = room;
    }

    public PlayerEntity createEntity(Player player) {
        Position3D startPosition = new Position3D(this.getRoom().getModel().getDoorX(), this.getRoom().getModel().getDoorY(), this.getRoom().getModel().getDoorZ());

        PlayerEntity entity = new PlayerEntity(player, this.getFreeId(), startPosition, this.getRoom());
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
