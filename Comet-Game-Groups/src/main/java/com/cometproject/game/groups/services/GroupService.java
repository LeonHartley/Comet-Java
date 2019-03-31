package com.cometproject.game.groups.services;

import com.cometproject.api.caching.Cache;
import com.cometproject.api.game.groups.IGroupItemService;
import com.cometproject.api.game.groups.IGroupService;
import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.IForumComponent;
import com.cometproject.api.game.groups.types.components.forum.ForumPermission;
import com.cometproject.api.game.groups.types.components.forum.IForumSettings;
import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.game.groups.types.components.membership.GroupAccessLevel;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.game.groups.factories.GroupFactory;
import com.cometproject.game.groups.types.components.ForumComponent;
import com.cometproject.storage.api.data.Data;
import com.cometproject.storage.api.repositories.IGroupForumRepository;
import com.cometproject.storage.api.repositories.IGroupMemberRepository;
import com.cometproject.storage.api.repositories.IGroupRepository;
import com.cometproject.storage.mysql.models.factories.GroupForumSettingsFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

public class GroupService implements IGroupService {

    private final Cache<Integer, IGroup> groupCache;
    private final Cache<Integer, IGroupData> groupDataCache;

    private IGroupItemService groupItemService;

    private final IGroupMemberRepository groupMemberRepository;
    private final IGroupRepository groupRepository;
    private final IGroupForumRepository groupForumRepository;

    private final GroupFactory groupFactory;

    public GroupService(Cache<Integer, IGroup> groupCache, Cache<Integer, IGroupData> groupDataCache,
                        IGroupItemService groupItemService, IGroupRepository groupRepository,
                        IGroupMemberRepository groupMemberRepository, IGroupForumRepository groupForumRepository) {
        this.groupCache = groupCache;
        this.groupDataCache = groupDataCache;
        this.groupItemService = groupItemService;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupForumRepository = groupForumRepository;

        this.groupFactory = new GroupFactory(this);
    }

    @Override
    public IGroupData getData(final int groupId) {
        if(groupId == 0) {
            return null;
        }

        if (this.groupDataCache.contains(groupId)) {
            return this.groupDataCache.get(groupId);
        }

        final Data<IGroupData> data = new Data<>();

        this.groupRepository.getDataById(groupId, data::set);

        if (data.has()) {
            this.groupDataCache.add(groupId, data.get());
        }

        return data.get();
    }

    @Override
    public IGroup getGroup(final int groupId) {
        if (groupId == 0) {
            return null;
        }

        if (this.groupCache.contains(groupId)) {
            return this.groupCache.get(groupId);
        }

        final IGroupData groupData = this.getData(groupId);

        if (groupData == null) {
            return null;
        }

        final Data<List<IGroupMember>> groupMemberData = new Data<>();
        final Data<List<Integer>> requestsData = new Data<>();

        this.groupMemberRepository.getAllByGroupId(groupId, groupMemberData::set);
        this.groupMemberRepository.getAllRequests(groupId, requestsData::set);

        if (!groupMemberData.has() || !requestsData.has()) {
            return null;
        }

        boolean foundOwner = false;

        for(IGroupMember member : groupMemberData.get()) {
            if(member.getPlayerId() == groupData.getOwnerId()) {
                foundOwner = true;
            }
        }

        if(!foundOwner) {
            this.groupMemberRepository.create(groupId, groupData.getOwnerId(), GroupAccessLevel.OWNER,
                    (member) -> groupMemberData.get().add(member));
        }

        return build(groupMemberData.get(), requestsData.get(), groupData);
    }

    @Override
    public void saveGroupData(IGroupData groupData) {
        // Queue to save?
        this.groupRepository.saveGroupData(groupData);
    }

    @Override
    public void createForumSettings(IForumComponent forumComponent) {
        // Queue to save?
        this.groupRepository.createForumSettings(forumComponent);
    }

    @Override
    public void addForum(IGroup group) {
        group.getData().setHasForum(true);
        this.saveGroupData(group.getData());

        // Add forum component to the group. - me deprimo ken te lo juro
        Map<Integer, IForumThread> forumThreads = new HashMap<>();
        List<Integer> pinnedThreads = new ArrayList<>();

        ForumComponent forumComponent = new ForumComponent(
                GroupForumSettingsFactory.createSettings(
                        group.getData().getId(),
                        ForumPermission.getById(0),
                        ForumPermission.getById(1),
                        ForumPermission.getById(1),
                        ForumPermission.getById(2)),
                pinnedThreads, forumThreads);

        group.setForum(forumComponent);

        this.createForumSettings(forumComponent);
    }

    @Override
    public IGroup createGroup(IGroupData groupData, int ownerId) {
        final List<IGroupMember> groupMembers = Lists.newArrayList();
        final List<Integer> requests = Lists.newArrayList();

        this.groupRepository.create(groupData);

        this.groupMemberRepository.create(groupData.getId(), ownerId, GroupAccessLevel.OWNER, groupMembers::add);
        return this.build(groupMembers, requests, groupData);
    }

    @Override
    public void addGroupMember(IGroup group, IGroupMember groupMember) {
        if (groupMember.getMembershipId() == 0) {
            this.groupMemberRepository.create(group.getId(), groupMember.getPlayerId(), groupMember.getAccessLevel(), (member) -> {
                groupMember.setMembershipId(member.getMembershipId());
            });
        }

        group.getMembers().getAll().remove(groupMember.getPlayerId());

        if (groupMember.getAccessLevel().isAdmin()) {
            group.getMembers().getAdministrators().add(groupMember.getPlayerId());
        }

        group.getMembers().getAll().put(groupMember.getPlayerId(), groupMember);
    }

    @Override
    public void removeGroupMember(IGroup group, IGroupMember groupMember) {
        this.groupMemberRepository.delete(groupMember.getMembershipId());

        if(groupMember.getAccessLevel().isAdmin()) {
            group.getMembers().getAdministrators().remove(groupMember.getPlayerId());
        }

        group.getMembers().getAll().remove(groupMember.getPlayerId());
    }

    @Override
    public void createRequest(IGroup group, int playerId) {
        if (group.getMembers().hasMembership(playerId) ||
                group.getMembers().getMembershipRequests().contains(playerId)) {
            return;
        }

        this.groupMemberRepository.createRequest(group.getId(), playerId);
        group.getMembers().getMembershipRequests().add(playerId);
    }

    @Override
    public void removeRequest(IGroup group, int playerId) {
        if (!group.getMembers().getMembershipRequests().contains(playerId)) {
            return;
        }

        this.groupMemberRepository.deleteRequest(group.getId(), playerId);
        group.getMembers().getMembershipRequests().remove(playerId);
    }

    @Override
    public void clearRequests(IGroup group) {
        this.groupMemberRepository.clearRequests(group.getId());

        group.getMembers().getMembershipRequests().clear();
    }

    @Override
    public void saveForumSettings(IForumSettings forumSettings) {
        this.groupForumRepository.saveSettings(forumSettings);
    }

    private IGroup build(List<IGroupMember> groupMemberData, List<Integer> requestsData,
                         IGroupData groupData) {
        final Map<Integer, IGroupMember> groupMembers = Maps.newConcurrentMap();
        final Set<Integer> requests = Sets.newConcurrentHashSet();
        final Set<Integer> administrators = Sets.newConcurrentHashSet();

        requests.addAll(requestsData);

        for (final IGroupMember groupMember : groupMemberData) {
            if (groupMember.getAccessLevel().isAdmin())
                administrators.add(groupMember.getPlayerId());

            groupMembers.put(groupMember.getPlayerId(), groupMember);
        }

        groupMemberData.clear();
        requestsData.clear();

        IForumSettings forumSettings = null;
        Map<Integer, IForumThread> forumThreads = null;
        List<Integer> pinnedThreads = null;

        if (groupData.hasForum()) {
            final Data<IForumSettings> forumSettingsData = new Data<>();
            final Data<Map<Integer, IForumThread>> forumThreadData = new Data<>();
            final Data<List<Integer>> pinnedThreadData = new Data<>();

            this.groupForumRepository.getSettingsByGroupId(groupData.getId(), forumSettingsData::set);

            if (forumSettingsData.has()) {
                forumSettings = forumSettingsData.get();

                // Now we have the forum settings, we can load the rest of the forum data
                this.groupForumRepository.getAllMessages(groupData.getId(), (threads, pinned) -> {
                    forumThreadData.set(threads);
                    pinnedThreadData.set(pinned);
                });

                if (forumThreadData.has() && pinnedThreadData.has()) {
                    forumThreads = forumThreadData.get();
                    pinnedThreads = pinnedThreadData.get();
                }
            }
        }

        final IGroup group = this.groupFactory.createGroupInstance(groupData, groupMembers, requests,
                administrators, forumSettings, pinnedThreads, forumThreads);

        this.groupCache.add(groupData.getId(), group);

        return group;
    }

    @Override
    public IGroupItemService getItemService() {
        return this.groupItemService;
    }

    @Override
    public void setItemService(IGroupItemService itemService) {
        this.groupItemService = itemService;
    }

    @Override
    public void removeGroup(int id) {
        final IGroup group = this.getGroup(id);

        if (group == null) {
            return;
        }

        group.dispose();

        this.groupRepository.deleteGroup(id);

        this.groupCache.remove(id);
        this.groupDataCache.remove(id);
    }
}
