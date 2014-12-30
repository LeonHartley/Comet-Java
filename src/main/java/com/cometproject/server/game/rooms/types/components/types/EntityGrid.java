package com.cometproject.server.game.rooms.types.components.types;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;

import java.util.*;

public class EntityGrid implements Map<Position, List<GenericEntity>> {

    private final Map<Position, List<GenericEntity>> entities;

    public EntityGrid() {
        this.entities = new HashMap<>();
    }

    @Override
    public int size() {
        return this.entities.size();
    }

    @Override
    public boolean isEmpty() {
        return this.entities.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        for(Position position : this.entities.keySet()) {
            if(position.equals(key)) {
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public List<GenericEntity> get(Object key) {
        for(Map.Entry<Position, List<GenericEntity>> entry : this.entities.entrySet()) {
            if(entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }

        return new ArrayList<>();
    }

    @Override
    public List<GenericEntity> put(Position key, List<GenericEntity> value) {
        if(!this.containsKey(key)) {
            this.entities.put(key, value);
        }

        return value;
    }

    @Override
    public List<GenericEntity> remove(Object key) {
        if(!this.containsKey(key)) {
            return null;
        }

        List<GenericEntity> entities = this.get(key);
        Position posKeyToRemove = null;

        for(Position posKey : this.entities.keySet()) {
            if(posKey.equals(key)) {
                posKeyToRemove = posKey;
            }
        }

        if(posKeyToRemove != null) {
            this.entities.remove(posKeyToRemove);
        }

        return entities;
    }

    @Override
    public void putAll(Map<? extends Position, ? extends List<GenericEntity>> m) {

    }

    @Override
    public void clear() {
        this.entities.clear();
    }

    @Override
    public Set<Position> keySet() {
        return null;
    }

    @Override
    public Collection<List<GenericEntity>> values() {
        return null;
    }

    @Override
    public Set<Entry<Position, List<GenericEntity>>> entrySet() {
        return null;
    }
}
