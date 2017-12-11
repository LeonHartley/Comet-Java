package com.cometproject.game.groups.services;

import com.cometproject.api.caching.Cache;
import com.cometproject.api.game.groups.IGroupItemService;
import com.cometproject.api.game.groups.IGroupService;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.storage.api.data.Data;
import com.cometproject.storage.api.repositories.IGroupMemberRepository;
import com.cometproject.storage.api.repositories.IGroupRepository;

public class GroupService implements IGroupService {

    private final Cache<Integer, IGroup> groupCache;
    private final Cache<Integer, IGroupData> groupDataCache;

    private final IGroupItemService groupItemService;

    private final IGroupMemberRepository groupMemberRepository;
    private final IGroupRepository groupRepository;

    public GroupService(Cache<Integer, IGroup> groupCache, Cache<Integer, IGroupData> groupDataCache,
                        IGroupItemService groupItemService, IGroupRepository groupRepository,
                        IGroupMemberRepository groupMemberRepository) {
        this.groupCache = groupCache;
        this.groupDataCache = groupDataCache;
        this.groupItemService = groupItemService;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    @Override
    public IGroupData getData(final int groupId) {
        if(this.groupDataCache.contains(groupId)) {
            return this.groupDataCache.get(groupId);
        }

        final Data<IGroupData> data = new Data<>();

        this.groupRepository.getDataById(groupId, data::set);

        if(data.has()) {
            this.groupDataCache.add(groupId, data.get());
        }

        return data.get();
    }

    @Override
    public IGroup getGroup(final int groupId) {
        if(groupId == 0) {
            return null;
        }

        if (this.groupCache.contains(groupId)) {
            return this.groupCache.get(groupId);
        }

        return null;
    }


    @Override
    public IGroupItemService getItemService() {
        return this.groupItemService;
    }
}
