package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class HotelMaintenanceMessageComposer {
    public static Composer compose(int hour, int minute, boolean logout) {
        Composer msg = new Composer(Composers.MaintenanceNotificationMessageComposer);

        msg.writeInt(hour);
        msg.writeInt(minute);
        msg.writeBoolean(logout);

        return msg;

    }
}
