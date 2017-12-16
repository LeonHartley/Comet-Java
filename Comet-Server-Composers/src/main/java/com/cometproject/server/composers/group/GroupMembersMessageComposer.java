package com.cometproject.server.composers.group;

import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.game.players.IPlayerService;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.api.networking.sessions.ISessionService;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.ArrayList;
import java.util.List;


public class GroupMembersMessageComposer extends MessageComposer {
    private static final int MEMBERS_PER_PAGE = 14;

    private final IGroupData group;
    private final int page;
    private final List<PlayerAvatar> groupMembers;
    private final int requestType;
    private final String searchQuery;
    private final boolean isAdmin;

    private final IPlayerService playerService;
    private final ISessionService sessionService;

    public GroupMembersMessageComposer(final IGroupData group, final int page, final List<PlayerAvatar> groupMembers,
                                       final int requestType, final String searchQuery, final boolean isAdmin,
                                       IPlayerService playerService, ISessionService sessionService) {
        this.group = group;
        this.page = page;
        this.groupMembers = groupMembers;
        this.requestType = requestType;
        this.searchQuery = searchQuery;
        this.isAdmin = isAdmin;

        this.playerService = playerService;
        this.sessionService = sessionService;
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
            List<List<PlayerAvatar>> paginatedMembers = paginateMembers(groupMembers, MEMBERS_PER_PAGE);

            msg.writeInt(paginatedMembers.get(page).size());

//
//
//            for (PlayerAvatar groupMember : paginatedMembers.get(page)) {
//                int playerId;
//                int joinDate = 0;
//
//                    if (requestType == 1) {
//                        msg.writeInt(playerId == group.getOwnerId() ? 0 : 1);
//                    } else {
//                        msg.writeInt(3);
//                    }
//
//                    if (((IGroupMember) memberObject).getAccessLevel().isAdmin()) {
//                        msg.writeInt(group.getOwnerId() == ((IGroupMember) memberObject).getPlayerId() ? 0 : 1);
//                    } else {
//                        msg.writeInt(2);
//                    }
//
//
//                PlayerAvatar playerAvatar = null;
//                    msg.writeInt(playerId);
//                    msg.writeString(playerAvatar.getUsername());
//                    msg.writeString(playerAvatar.getFigure());
//
//                msg.writeString(joinDate != 0 ? GroupInformationMessageComposer.getDate(joinDate) : "");
//            }
        }

        msg.writeBoolean(isAdmin);
        msg.writeInt(MEMBERS_PER_PAGE);
        msg.writeInt(page);

        msg.writeInt(requestType);
        msg.writeString(searchQuery);
    }

    private List<List<PlayerAvatar>> paginateMembers(List<PlayerAvatar> originalList, int chunkSize) {
        List<List<PlayerAvatar>> listOfChunks = new ArrayList<>();

        for (int i = 0; i < originalList.size() / chunkSize; i++) {
            listOfChunks.add(originalList.subList(i * chunkSize, i * chunkSize + chunkSize));
        }

        if (originalList.size() % chunkSize != 0) {
            listOfChunks.add(originalList.subList(originalList.size() - originalList.size() % chunkSize, originalList.size()));
        }

        return listOfChunks;
    }
}
