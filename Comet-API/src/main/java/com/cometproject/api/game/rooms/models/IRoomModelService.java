package com.cometproject.api.game.rooms.models;

public interface IRoomModelService {

    void loadModels();

    IRoomModel getModel(String id);

    IRoomModelFactory getRoomModelFactory();
}
