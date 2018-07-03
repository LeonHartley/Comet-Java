package com.cometproject.storage.api.repositories;

public interface IPhotoRepository {
    void savePhoto(int playerId, int roomId, String photoId, int timestamp);
}
