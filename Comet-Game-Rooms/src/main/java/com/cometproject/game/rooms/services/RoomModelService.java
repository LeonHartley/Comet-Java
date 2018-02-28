package com.cometproject.game.rooms.services;

import com.cometproject.api.game.rooms.models.*;
import com.cometproject.game.rooms.models.RoomModel;
import com.cometproject.storage.api.repositories.IRoomRepository;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.util.Map;

public class RoomModelService implements IRoomModelService {
    private static final Logger log = Logger.getLogger(RoomModelService.class);

    private final IRoomRepository roomRepository;
    private final IRoomModelFactory roomModelFactory;

    private final Map<String, IRoomModel> models;

    public RoomModelService(IRoomModelFactory roomModelFactory, IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
        this.roomModelFactory = roomModelFactory;

        this.models = Maps.newConcurrentMap();
    }

    @Override
    public void loadModels() {
        this.models.clear();

        this.roomRepository.getAllModels((modelData) -> {
            for (Map.Entry<String, RoomModelData> roomModelData : modelData.entrySet()) {
                try {
                    final IRoomModel roomModel = this.roomModelFactory.createModel(roomModelData.getValue());

                    if (roomModel != null) {
                        this.models.put(roomModelData.getKey(), roomModel);
                    }
                } catch (InvalidModelException e) {
                    log.error("Failed to load model " + roomModelData.getKey(), e);
                }
            }

            log.info("Loaded " + this.models.size() + " static room models");
        });
    }

    @Override
    public IRoomModel getModel(String id) {
        return this.models.get(id);
    }

    @Override
    public IRoomModelFactory getRoomModelFactory() {
        return this.roomModelFactory;
    }
}
