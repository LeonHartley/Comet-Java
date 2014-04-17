package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.navigator.types.featured.BannerType;
import com.cometproject.server.game.navigator.types.featured.FeaturedRoom;
import com.cometproject.server.game.navigator.types.featured.ImageType;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddToStaffPickedRoomsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if (!client.getPlayer().getPermissions().hasPermission("room_staff_pick")) {
            return;
        }

        if (client.getPlayer().getEntity() == null)
            return;

        Room room = client.getPlayer().getEntity().getRoom();

        if (GameEngine.getNavigator().isFeatured(room.getId())) {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE navigator_featured_rooms SET enabled = '0' WHERE room_id = ?");
            statement.setInt(1, room.getId());

            statement.execute();

            GameEngine.getNavigator().getFeaturedRooms().remove(GameEngine.getNavigator().getFeaturedRoomById(room.getId()));
            client.send(AdvancedAlertMessageComposer.compose(Locale.get("navigator.staff.picks.removed.title"), Locale.get("navigator.staff.picks.removed.message")));
            return;
        }

        PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into navigator_featured_rooms VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", true);

        statement.setString(1, "small");
        statement.setString(2, room.getData().getName());
        statement.setString(3, room.getData().getDescription());
        statement.setString(4, "");
        statement.setString(5, "internal");
        statement.setInt(6, room.getId());
        statement.setInt(7, 1); // ID OF STAFF PICKED ROOMS CATEGORY!!!
        statement.setString(8, "1");
        statement.setString(9, "0"); // recommended
        statement.setString(10, "room");

        statement.execute();

        ResultSet keys = statement.getGeneratedKeys();
        int id = 0;

        while (keys.next()) {
            id = keys.getInt(1);
        }

        GameEngine.getNavigator().getFeaturedRooms().add(new FeaturedRoom(id, BannerType.SMALL, room.getData().getName(), room.getData().getDescription(), "", ImageType.INTERNAL, room.getId(), 1, true, false, false));
        client.send(AdvancedAlertMessageComposer.compose(Locale.get("navigator.staff.picks.added.title"), Locale.get("navigator.staff.picks.added.message")));
    }
}
