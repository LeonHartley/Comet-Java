package com.cometproject.game.groups.services;

import com.cometproject.api.caching.Cache;
import com.cometproject.api.game.groups.IGroupItemService;
import com.cometproject.api.game.groups.IGroupService;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.membership.GroupAccessLevel;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.game.groups.factories.GroupFactory;
import com.cometproject.storage.api.data.Data;
import com.cometproject.storage.api.repositories.IGroupMemberRepository;
import com.cometproject.storage.api.repositories.IGroupRepository;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupService implements IGroupService {

    private final Cache<Integer, IGroup> groupCache;
    private final Cache<Integer, IGroupData> groupDataCache;

    private final IGroupItemService groupItemService;

    private final IGroupMemberRepository groupMemberRepository;
    private final IGroupRepository groupRepository;

    private final GroupFactory groupFactory;

    public GroupService(Cache<Integer, IGroup> groupCache, Cache<Integer, IGroupData> groupDataCache,
                        IGroupItemService groupItemService, IGroupRepository groupRepository,
                        IGroupMemberRepository groupMemberRepository) {
        this.groupCache = groupCache;
        this.groupDataCache = groupDataCache;
        this.groupItemService = groupItemService;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;

        this.groupFactory = new GroupFactory(this);
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

        final IGroupData groupData = this.getData(groupId);

        if(groupData == null) {
            return null;
        }

        final Data<List<IGroupMember>> groupMemberData = new Data<>();
        final Data<List<Integer>> requestsData = new Data<>();

        this.groupMemberRepository.getAllByGroupId(groupId, groupMemberData::set);
        this.groupMemberRepository.getAllRequests(groupId, requestsData::set);

        if(!groupMemberData.has() || !requestsData.has()) {
            return null;
        }

        return build(groupMemberData, requestsData, groupData);
    }

    @Override
    public void addGroupMember(IGroup group, IGroupMember groupMember) {
        if(groupMember.getMembershipId() == 0) {
            this.groupMemberRepository.create(groupMember);
        }

        if(group.getMembers().getAll().containsKey(groupMember.getPlayerId())) {
            group.getMembers().getAll().remove(groupMember.getPlayerId());
        }

        if(groupMember.getAccessLevel().isAdmin()) {
            group.getMembers().getAdministrators().add(groupMember.getPlayerId());
        }

        group.getMembers().getAll().put(groupMember.getPlayerId(), groupMember);
    }

    @Override
    public void removeGroupMember(IGroup group, IGroupMember groupMember) {
        this.groupMemberRepository.delete(groupMember.getMembershipId());

        group.getMembers().getAll().remove(groupMember.getPlayerId());
    }

    @Override
    public void createRequest(IGroup group, int playerId) {
        if(group.getMembers().hasMembership(playerId) ||
                group.getMembers().getMembershipRequests().contains(playerId)) {
            return;
        }

        this.groupMemberRepository.createRequest(group.getId(), playerId);
        group.getMembers().getMembershipRequests().add(playerId);
    }

    @Override
    public void removeRequest(IGroup group, int playerId) {
        if(!group.getMembers().getMembershipRequests().contains(playerId)) {
            return;
        }

        this.groupMemberRepository.deleteRequest(group.getId(), playerId);
    }

    @Override
    public void clearRequests(IGroup group) {
        this.groupMemberRepository.clearRequests(group.getId());

        group.getMembers().getMembershipRequests().clear();
    }

    private IGroup build(Data<List<IGroupMember>> groupMemberData, Data<List<Integer>> requestsData,
                         IGroupData groupData) {
        final Map<Integer, IGroupMember> groupMembers = Maps.newConcurrentMap();
        final Set<Integer> requests = Sets.newConcurrentHashSet();
        final Set<Integer> administrators = Sets.newConcurrentHashSet();

        requests.addAll(requestsData.get());

        for(final IGroupMember groupMember : groupMemberData.get()) {
            if(groupMember.getAccessLevel() == GroupAccessLevel.ADMIN)
                administrators.add(groupMember.getPlayerId());

            groupMembers.put(groupMember.getMembershipId(), groupMember);
        }

        groupMemberData.get().clear();
        requestsData.get().clear();

        final IGroup group = this.groupFactory.createGroupInstance(groupData, groupMembers, requests,
                administrators);

        this.groupCache.add(groupData.getId(), group);

        return group;
    }

    @Override
    public IGroupItemService getItemService() {
        return this.groupItemService;
    }
}
