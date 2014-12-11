package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.navigator.types.featured.BannerType;
import com.cometproject.server.game.navigator.types.featured.FeaturedRoom;
import com.cometproject.server.game.navigator.types.featured.ImageType;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.navigator.NavigatorDao;


public class AddToStaffPickedRoomsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if (!client.getPlayer().getPermissions().hasPermission("room_staff_pick")) {
            return;
        }

        if (client.getPlayer().getEntity() == null)
            return;

        Room room = client.getPlayer().getEntity().getRoom();

        if (NavigatorManager.getInstance().isFeatured(room.getId())) {
            NavigatorDao.disableFeaturedRoom(room.getId());

            NavigatorManager.getInstance().getFeaturedRooms().remove(NavigatorManager.getInstance().getFeaturedRoomById(room.getId()));
            client.send(AdvancedAlertMessageComposer.compose(Locale.get("navigator.staff.picks.removed.title"), Locale.get("navigator.staff.picks.removed.message")));
            room.getEntities().broadcastMessage(RoomDataMessageComposer.compose(room));
            return;
        }

        int id = NavigatorDao.staffPick(room.getId(), room.getData().getName(), room.getData().getDescription());

        NavigatorManager.getInstance().getFeaturedRooms().add(new FeaturedRoom(id, BannerType.SMALL, room.getData().getName(), room.getData().getDescription(), "", ImageType.INTERNAL, room.getId(), 1, true, false, false));
        client.send(AdvancedAlertMessageComposer.compose(Locale.get("navigator.staff.picks.added.title"), Locale.get("navigator.staff.picks.added.message")));
        room.getEntities().broadcastMessage(RoomDataMessageComposer.compose(room));
    }
}
