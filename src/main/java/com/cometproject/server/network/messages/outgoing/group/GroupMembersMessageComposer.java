package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.ArrayList;
import java.util.List;

public class GroupMembersMessageComposer {
    private static final int MEMBERS_PER_PAGE = 14;

    public static Composer compose(GroupData group, int page, List<Object> groupMembers, int requestType, String searchQuery, boolean isOwner) {
        Composer msg = new Composer(Composers.GroupMembersMessageComposer);

        msg.writeInt(group.getId());
        msg.writeString(group.getTitle());
        msg.writeInt(group.getRoomId());
        msg.writeString(group.getBadge());

        msg.writeInt(groupMembers.size());

        List<List<Object>> paginatedMembers = paginateMembers(groupMembers, MEMBERS_PER_PAGE);

        msg.writeInt(paginatedMembers.get(page).size());

        for(Object memberObject : paginatedMembers.get(page)) {
            if(memberObject instanceof Integer) {
                // TODO: It's a request/admin, handle differently...
                continue;
            }

            GroupMember groupMember = (GroupMember) memberObject;

            if (groupMember.getAccessLevel().isAdmin()) {
                msg.writeInt(group.getOwnerId() == groupMember.getPlayerId() ? 0 : 1);
            } else {
                msg.writeInt(2);
            }

            PlayerData playerData = PlayerDao.getDataById(groupMember.getPlayerId());

            msg.writeInt(playerData.getId());
            msg.writeString(playerData.getUsername());
            msg.writeString(playerData.getFigure());
            msg.writeString(GroupInformationMessageComposer.getDate(groupMember.getDateJoined()));
        }

        msg.writeBoolean(isOwner);
        msg.writeInt(MEMBERS_PER_PAGE);
        msg.writeInt(page);

        msg.writeInt(requestType);
        msg.writeString(searchQuery);
        return msg;
    }

    private static List<List<Object>> paginateMembers(List<Object> originalList, int chunkSize) {
        List<List<Object>> listOfChunks = new ArrayList<>();

        for (int i = 0; i < originalList.size() / chunkSize; i++) {
            listOfChunks.add(originalList.subList(i * chunkSize, i * chunkSize + chunkSize));
        }

        if (originalList.size() % chunkSize != 0) {
            listOfChunks.add(originalList.subList(originalList.size() - originalList.size() % chunkSize, originalList.size()));
        }

        return listOfChunks;
    }
}









































