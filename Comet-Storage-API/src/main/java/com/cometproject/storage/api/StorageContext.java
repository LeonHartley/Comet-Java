package com.cometproject.storage.api;

import com.cometproject.storage.api.repositories.*;

public final class StorageContext {
    private static StorageContext storageContext;

    private IGroupRepository groupRepository;
    private IGroupMemberRepository groupMemberRepository;
    private IGroupForumRepository groupForumRepository;

    private IRoomItemRepository roomItemRepository;
    private IRoomRepository roomRepository;
    private IInventoryRepository inventoryRepository;
    private IRewardRepository rewardRepository;
    private IPhotoRepository photoRepository;

    public IGroupRepository getGroupRepository() {
        return groupRepository;
    }

    public void setGroupRepository(IGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public IGroupMemberRepository getGroupMemberRepository() {
        return groupMemberRepository;
    }

    public void setGroupMemberRepository(IGroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    public static void setCurrentContext(StorageContext ctx) {
        storageContext = ctx;
    }

    public static StorageContext getCurrentContext() {
        return storageContext;
    }

    public IGroupForumRepository getGroupForumRepository() {
        return groupForumRepository;
    }

    public void setGroupForumRepository(IGroupForumRepository groupForumRepository) {
        this.groupForumRepository = groupForumRepository;
    }

    public IRoomItemRepository getRoomItemRepository() {
        return roomItemRepository;
    }

    public void setRoomItemRepository(IRoomItemRepository roomItemRepository) {
        this.roomItemRepository = roomItemRepository;
    }

    public IInventoryRepository getInventoryRepository() {
        return inventoryRepository;
    }

    public void setInventoryRepository(IInventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public IRoomRepository getRoomRepository() {
        return roomRepository;
    }

    public void setRoomRepository(IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public IRewardRepository getRewardRepository() {
        return rewardRepository;
    }

    public void setRewardRepository(IRewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    public IPhotoRepository getPhotoRepository() {
        return photoRepository;
    }

    public void setPhotoRepository(IPhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }
}
