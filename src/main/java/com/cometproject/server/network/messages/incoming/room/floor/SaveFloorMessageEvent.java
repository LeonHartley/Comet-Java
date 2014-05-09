package com.cometproject.server.network.messages.incoming.room.floor;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class SaveFloorMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) throws Exception {
        String model = msg.readString();

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        String[] modelData = model.split(String.valueOf((char) 13));

        int lastLineLength = 0;
        boolean isValid = true;

        for (int i = 0; i < modelData.length; i++) {
            if (lastLineLength == 0) {
                lastLineLength = modelData[i].length();
                continue;
            }

            if (lastLineLength != modelData[i].length()) {
                isValid = false;
            }
        }

        if (!isValid) {
            client.send(AdvancedAlertMessageComposer.compose("Illegal Model", Locale.get("command.floor.invalid")));
            return;
        }

        room.getData().setHeightmap(model);

        client.send(AdvancedAlertMessageComposer.compose("Model Saved", Locale.get("command.floor.complete")));
        CometManager.getRooms().getGlobalProcessor().requestUnload(room.getId());
    }
}
