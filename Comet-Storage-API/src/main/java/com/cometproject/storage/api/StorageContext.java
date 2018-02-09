package com.cometproject.storage.api;

import com.cometproject.storage.api.repositories.IGroupForumRepository;
import com.cometproject.storage.api.repositories.IGroupMemberRepository;
import com.cometproject.storage.api.repositories.IGroupRepository;
import com.cometproject.storage.api.repositories.IRoomItemRepository;

public final class StorageContext {
    private static StorageContext storageContext;

    private IGroupRepository groupRepository;
    private IGroupMemberRepository groupMemberRepository;
    private IGroupForumRepository groupForumRepository;

    private IRoomItemRepository roomItemRepository;

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
}
