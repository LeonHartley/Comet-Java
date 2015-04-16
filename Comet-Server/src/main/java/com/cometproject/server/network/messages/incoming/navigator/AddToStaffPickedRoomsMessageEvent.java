package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.navigator.types.featured.BannerType;
import com.cometproject.server.game.navigator.types.featured.FeaturedRoom;
import com.cometproject.server.game.navigator.types.featured.ImageType;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.navigator.NavigatorDao;


public class AddToStaffPickedRoomsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (!client.getPlayer().getPermissions().hasPermission("room_staff_pick")) {
            return;
        }

        if (client.getPlayer().getEntity() == null)
            return;

        RoomInstance room = client.getPlayer().getEntity().getRoom();

        if (NavigatorManager.getInstance().isFeatured(room.getId())) {
            NavigatorDao.disableFeaturedRoom(room.getId());

            NavigatorManager.getInstance().getFeaturedRooms().remove(NavigatorManager.getInstance().getFeaturedRoomById(room.getId()));
            client.send(new AdvancedAlertMessageComposer(Locale.get("navigator.staff.picks.removed.title"), Locale.get("navigator.staff.picks.removed.message")));
            room.getEntities().broadcastMessage(new RoomDataMessageComposer(room));
            return;
        }

        int id = NavigatorDao.staffPick(room.getId(), room.getData().getName(), room.getData().getDescription());

        NavigatorManager.getInstance().getFeaturedRooms().add(new FeaturedRoom(id, BannerType.SMALL, room.getData().getName(), room.getData().getDescription(), "", ImageType.INTERNAL, room.getId(), 1, true, false, false));
        client.send(new AdvancedAlertMessageComposer(Locale.get("navigator.staff.picks.added.title"), Locale.get("navigator.staff.picks.added.message")));
        room.getEntities().broadcastMessage(new RoomDataMessageComposer(room));
    }
}
