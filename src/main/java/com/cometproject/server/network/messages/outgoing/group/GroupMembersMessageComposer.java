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

    public static Composer compose(GroupData group, int page, List<Object> groupMembers, int requestType, String searchQuery, boolean isAdmin) {
        Composer msg = new Composer(Composers.GroupMembersMessageComposer);

        msg.writeInt(group.getId());
        msg.writeString(group.getTitle());
        msg.writeInt(group.getRoomId());
        msg.writeString(group.getBadge());

        msg.writeInt(groupMembers.size());

        if (groupMembers.size() == 0) {
            msg.writeInt(0);
        } else {
            List<List<Object>> paginatedMembers = paginateMembers(groupMembers, MEMBERS_PER_PAGE);

            msg.writeInt(paginatedMembers.get(page).size());

            for (Object memberObject : paginatedMembers.get(page)) {
                int playerId;
                int joinDate = 0;

                if (memberObject instanceof Integer) {
                    playerId = (int) memberObject;

                    if (requestType == 1) {
                        msg.writeInt(playerId == group.getOwnerId() ? 0 : 1);
                    } else {
                        msg.writeInt(3);
                    }
                } else {
                    playerId = ((GroupMember) memberObject).getPlayerId();

                    if (((GroupMember) memberObject).getAccessLevel().isAdmin()) {
                        msg.writeInt(group.getOwnerId() == ((GroupMember) memberObject).getPlayerId() ? 0 : 1);
                    } else {
                        msg.writeInt(2);
                    }

                    joinDate = ((GroupMember) memberObject).getDateJoined();
                }

                PlayerData playerData = PlayerDao.getDataById(playerId);

                msg.writeInt(playerData.getId());
                msg.writeString(playerData.getUsername());
                msg.writeString(playerData.getFigure());
                msg.writeString(joinDate != 0 ? GroupInformationMessageComposer.getDate(joinDate) : "");
            }
        }

        msg.writeBoolean(isAdmin);
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
