package com.cometproject.network.messages.incoming.room.action;

import com.cometproject.boot.Comet;
import com.cometproject.game.rooms.types.Room;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.network.messages.outgoing.room.avatar.GiveRespectMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class RespectUserMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int respect = msg.readInt();

        if(respect == client.getPlayer().getEntity().getVirtualId()) {
            return;
        }

        Session user = Comet.getServer().getNetwork().getSessions().getByPlayerId(respect);
        Room room = client.getPlayer().getEntity().getRoom();

        if(user == null || user.getPlayer() == null) {
            return;
        }

        if(client.getPlayer().getStats().getDailyRespects() < 1) {
            return;
        }

        user.getPlayer().getStats().incrementRespectPoints(1);
        client.getPlayer().getStats().decrementDailyRespects(1);

        room.getEntities().broadcastMessage(ActionMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), 7));
        room.getEntities().broadcastMessage(GiveRespectMessageComposer.compose(user.getPlayer().getEntity().getVirtualId(), user.getPlayer().getStats().getRespectPoints()));
    }
}
