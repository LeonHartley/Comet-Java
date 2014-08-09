package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.groups.GroupElementsMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.groups.GroupPartsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class BuyGroupDialogMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if(!Comet.isDebugging) {
            client.send(AdvancedAlertMessageComposer.compose("Feature Disabled", "This feature is still being developed and is not ready for the public yet.<br><br>If you want access to features such as this before other users, you may be interested in joining the beta program. <br><br>Contact a hotel manager for more details."));
            return;
        }

        client.send(GroupPartsMessageComposer.compose(client.getPlayer().getRooms()));
        client.send(GroupElementsMessageComposer.compose());
    }
}
