package com.cometsrv.game.rooms.types.components;

import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.types.Room;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Map;

public class EntityComponent {
    private Room room;
    private Logger log = Logger.getLogger(EntityComponent.class.getName());
    private int idCounter = 0;

    private Map<Integer, GenericEntity> entities;

    public EntityComponent(Room room) {
        this.room = room;
        this.entities = new FastMap<>();
    }

    public void dispose() {
        for(GenericEntity entity : entities.values()) {
            entity.leaveRoom();
        }

        entities.clear();
    }

    public void add(GenericEntity entity) {
        this.entities.put(entity.getId(), entity);
    }

    public int getFreeId() {
        return idCounter++;
    }
}
