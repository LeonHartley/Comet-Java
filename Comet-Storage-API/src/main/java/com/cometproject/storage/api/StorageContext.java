package com.cometproject.storage.api;

import com.cometproject.storage.api.repositories.groups.IGroupForumRepository;
import com.cometproject.storage.api.repositories.groups.IGroupMemberRepository;
import com.cometproject.storage.api.repositories.groups.IGroupRepository;
import com.cometproject.storage.api.repositories.landing.IHallOfFameRepository;
import com.cometproject.storage.api.repositories.landing.IPromoArticleRepository;

public final class StorageContext {
    private static StorageContext storageContext;

    /**
     * Groups
     */
    private IGroupRepository groupRepository;
    private IGroupMemberRepository groupMemberRepository;
    private IGroupForumRepository groupForumRepository;

    /**
     * Landing
     */
    private IPromoArticleRepository promoArticleRepository;
    private IHallOfFameRepository hallOfFameRepository;

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
    public IPromoArticleRepository getPromoArticleRepository() {
        return promoArticleRepository;
    }

    public void setPromoArticleRepository(IPromoArticleRepository promoArticleRepository) {
        this.promoArticleRepository = promoArticleRepository;
    }

    public IHallOfFameRepository getHallOfFameRepository() {
        return hallOfFameRepository;
    }

    public void setHallOfFameRepository(IHallOfFameRepository hallOfFameRepository) {
        this.hallOfFameRepository = hallOfFameRepository;
    }
}
