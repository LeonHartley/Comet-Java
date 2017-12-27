package com.cometproject.server.game.commands.staff.cache;

import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class ReloadGroupCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
//        if (params.length != 1) {
//            return;
//        }
//
//        if (!StringUtils.isNumeric(params[0])) {
//            return;
//        }
//
//        final int groupId = Integer.parseInt(params[0]);
//
//        final IGroupData groupData = GroupManager.getInstance().getData(groupId);
//
//        StorageContext.getCurrentContext().getGroupRepository().getDataById(groupId, newGroupData -> {
//            if (groupData.getRoomId() != newGroupData.getRoomId()) {
//                if (RoomManager.getInstance().isActive(groupData.getRoomId())) {
//                    Room oldRoom = RoomManager.getInstance().get(groupData.getRoomId());
//
//                    if (oldRoom != null) {
//                        oldRoom.setIdleNow();
//                    }
//                }
//
//                if (RoomManager.getInstance().isActive(newGroupData.getRoomId())) {
//                    Room newRoom = RoomManager.getInstance().get(newGroupData.getRoomId());
//
//                    if (newRoom != null) {
//                        newRoom.setIdleNow();
//                    }
//                }
//            }
//
//            groupData.setCanMembersDecorate(newGroupData.canMembersDecorate());
//            groupData.setOwnerId(newGroupData.getOwnerId());
//            groupData.setRoomId(newGroupData.getRoomId());
//            groupData.setBadge(newGroupData.getBadge());
//            groupData.setColourA(newGroupData.getColourA());
//            groupData.setColourB(newGroupData.getColourB());
//            groupData.setDescription(newGroupData.getDescription());
//            groupData.setTitle(newGroupData.getTitle());
//            groupData.setType(newGroupData.getType());
//
//            client.send(new AlertMessageComposer(Locale.getOrDefault("command.reloadgroup.done", "Group data reloaded successfully!")));
//        });
    }

    @Override
    public String getPermission() {
        return "reloadgroup_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
