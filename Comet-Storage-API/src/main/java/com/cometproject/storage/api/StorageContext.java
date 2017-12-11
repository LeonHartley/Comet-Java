package com.cometproject.storage.api;

import com.cometproject.storage.api.repositories.IGroupMemberRepository;
import com.cometproject.storage.api.repositories.IGroupRepository;

public final class StorageContext {
    private static StorageContext storageContext;

    private IGroupRepository groupRepository;
    private IGroupMemberRepository groupMemberRepository;

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
}