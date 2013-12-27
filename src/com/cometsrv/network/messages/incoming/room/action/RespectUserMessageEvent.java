package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometsrv.network.messages.outgoing.room.avatar.GiveRespectMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class RespectUserMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int respect = msg.readInt();

        if(respect == client.getPlayer().getId() || client.getPlayer().getStats().getDailyRespects() < 0)
            return;

        Session user = Comet.getServer().getNetwork().getSessions().getByPlayerId(respect);
        Room room = client.getPlayer().getAvatar().getRoom();

        if(user == null || user.getPlayer() == null)
            return;

        if(client.getPlayer().getStats().getDailyRespects() < 1) {
            return;
        }

        user.getPlayer().getStats().incrementRespectPoints(1);
        client.getPlayer().getStats().decrementDailyRespects(1);

        room.getAvatars().broadcast(ActionMessageComposer.compose(client.getPlayer().getId(), 7));
        room.getAvatars().broadcast(GiveRespectMessageComposer.compose(user.getPlayer().getId(), user.getPlayer().getStats().getRespectPoints()));
    }
}
