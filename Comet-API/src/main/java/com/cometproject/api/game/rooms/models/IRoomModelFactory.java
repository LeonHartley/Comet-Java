package com.cometproject.api.game.rooms.models;

public interface IRoomModelFactory {

    IRoomModel createModel(RoomModelData roomModelData) throws InvalidModelException;

}
