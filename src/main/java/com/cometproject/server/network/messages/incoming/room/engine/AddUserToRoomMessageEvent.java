package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.GroupBadgesMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.HandItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomPanelMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.ConfigureWallAndFloorMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastMap;

import java.util.Map;

public class AddUserToRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        PlayerEntity avatar = client.getPlayer().getEntity();

        if (avatar == null) {
            return;
        }

        Room room = avatar.getRoom();

        if (room == null) {
            return;
        }

        if (!room.getProcess().isActive()) {
            room.getProcess().start();
        }

        if (!room.getItemProcess().isActive()) {
            room.getItemProcess().start();
        }

        if (client.getPlayer().getFloodTime() >= 1) {
            client.send(FloodFilterMessageComposer.compose(client.getPlayer().getFloodTime()));
        }

        Map<Integer, String> groupsInRoom = new FastMap<>();

        for (PlayerEntity playerEntity : room.getEntities().getPlayerEntities()) {
            if (playerEntity.getPlayer().getData().getFavouriteGroup() != 0) {
                GroupData groupData = CometManager.getGroups().getData(playerEntity.getPlayer().getData().getFavouriteGroup());

                if (groupData == null)
                    continue;

                groupsInRoom.put(playerEntity.getPlayer().getData().getFavouriteGroup(), groupData.getBadge());
            }
        }

        client.send(GroupBadgesMessageComposer.compose(groupsInRoom));

        client.send(RoomPanelMessageComposer.compose(room.getId(), room.getData().getOwnerId() == client.getPlayer().getId() || client.getPlayer().getPermissions().hasPermission("room_full_control")));
        client.send(RoomDataMessageComposer.compose(room));

        client.send(AvatarsMessageComposer.compose(room));
        room.getEntities().broadcastMessage(AvatarsMessageComposer.compose(client.getPlayer().getEntity()));

        client.send(AvatarUpdateMessageComposer.compose(room));
        avatar.markNeedsUpdate();

        for (GenericEntity av : client.getPlayer().getEntity().getRoom().getEntities().getEntitiesCollection().values()) {
            if (av.getDanceId() != 0) {
                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(DanceMessageComposer.compose(av.getVirtualId(), av.getDanceId()));
            }

            if (av.getHandItem() != 0) {
                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(HandItemMessageComposer.compose(av.getVirtualId(), av.getHandItem()));
            }
        }

        client.send(ConfigureWallAndFloorMessageComposer.compose(client.getPlayer().getEntity().getRoom().getData().getHideWalls(), client.getPlayer().getEntity().getRoom().getData().getWallThickness(), client.getPlayer().getEntity().getRoom().getData().getFloorThickness()));
        client.getPlayer().getMessenger().sendStatus(true, true);

        groupsInRoom.clear();
    }
}
