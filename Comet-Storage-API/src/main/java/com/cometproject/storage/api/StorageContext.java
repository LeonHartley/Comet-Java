package com.cometproject.storage.api;

import com.cometproject.storage.api.repositories.IGroupRepository;

public final class StorageContext {
    private static StorageContext storageContext;

    private IGroupRepository groupRepository;

    public IGroupRepository getGroupRepository() {
        return groupRepository;
    }

    public void setGroupRepository(IGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public static StorageContext getCurrent() {
        return storageContext;
    }
}
