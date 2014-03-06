package com.cometproject.server.network.messages.incoming.room.item.mannequins;

import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.data.MannequinData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorExtraDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class SaveMannequinMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int id = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) {
            return;
        }

        FloorItem item = room.getItems().getFloorItem(id);

        if(item == null) {
            return;
        }

        MannequinData data = MannequinData.get(item.getExtraData());

        String figure = client.getPlayer().getData().getFigure();
        String gender = client.getPlayer().getData().getGender().toLowerCase();

        if(data == null) {
            data = new MannequinData("New Mannequin", figure, gender);
        } else {
            data.setFigure(figure);
            data.setGender(gender);
        }

        room.getEntities().broadcastMessage(UpdateFloorExtraDataMessageComposer.compose(item.getId(), MannequinData.get(data)));
        item.setExtraData(MannequinData.get(data));
        item.saveData();
    }
}
