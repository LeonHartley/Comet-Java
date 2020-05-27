package com.cometproject.server.composers.group;

import com.cometproject.api.game.groups.types.GroupMemberAvatar;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.ArrayList;
import java.util.List;


public class GroupMembersMessageComposer extends MessageComposer {
    private static final int MEMBERS_PER_PAGE = 14;

    private final IGroupData group;
    private final int page;
    private final List<GroupMemberAvatar> groupMembers;
    private final int requestType;
    private final String searchQuery;
    private final boolean isAdmin;

    public GroupMembersMessageComposer(final IGroupData group, final int page, final List<GroupMemberAvatar> groupMembers,
                                       final int requestType, final String searchQuery, final boolean isAdmin) {
        this.group = group;
        this.page = page;
        this.groupMembers = groupMembers;
        this.requestType = requestType;
        this.searchQuery = searchQuery;
        this.isAdmin = isAdmin;
    }

    @Override
    public short getId() {
        return Composers.GroupMembersMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(group.getId());
        msg.writeString(group.getTitle());
        msg.writeInt(group.getRoomId());
        msg.writeString(group.getBadge());

        msg.writeInt(groupMembers.size());

        if (groupMembers.size() == 0) {
            msg.writeInt(0);
        } else {
            List<List<GroupMemberAvatar>> paginatedMembers = paginateMembers(groupMembers, MEMBERS_PER_PAGE);

            msg.writeInt(paginatedMembers.get(page).size());

            int dateJoined = 0;

            for (GroupMemberAvatar groupMember : paginatedMembers.get(page)) {
                if (groupMember.getGroupMember() == null) {
                    if (requestType == 1) {
                        msg.writeInt(groupMember.getPlayerAvatar().getId() == group.getOwnerId() ? 0 : 1);
                    } else {
                        msg.writeInt(3);
                    }
                } else {
                    final IGroupMember member = groupMember.getGroupMember();

                    dateJoined = member.getDateJoined();

                    if (member.getAccessLevel().isAdmin()) {
                        msg.writeInt(group.getOwnerId() == groupMember.getPlayerAvatar().getId() ? 0 : 1);
                    } else {
                        if(requestType == 2) {
                            msg.writeInt(3);
                        } else {
                            msg.writeInt(2);
                        }
                    }
                }

                msg.writeInt(groupMember.getPlayerAvatar().getId());
                msg.writeString(groupMember.getPlayerAvatar().getUsername());
                msg.writeString(groupMember.getPlayerAvatar().getFigure());

                msg.writeString(groupMember.getPlayerAvatar() != null ? GroupInformationMessageComposer.getDate(dateJoined) : "");
            }

        }

        msg.writeBoolean(isAdmin);
        msg.writeInt(MEMBERS_PER_PAGE);
        msg.writeInt(page);

        msg.writeInt(requestType);
        msg.writeString(searchQuery);
    }

    private List<List<GroupMemberAvatar>> paginateMembers(List<GroupMemberAvatar> originalList, int chunkSize) {
        List<List<GroupMemberAvatar>> listOfChunks = new ArrayList<>();

        for (int i = 0; i < originalList.size() / chunkSize; i++) {
            listOfChunks.add(originalList.subList(i * chunkSize, i * chunkSize + chunkSize));
        }

        if (originalList.size() % chunkSize != 0) {
            listOfChunks.add(originalList.subList(originalList.size() - originalList.size() % chunkSize, originalList.size()));
        }

        return listOfChunks;
    }
}
