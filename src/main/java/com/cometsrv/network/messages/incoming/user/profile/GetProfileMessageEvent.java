package com.cometsrv.network.messages.incoming.user.profile;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.players.data.PlayerData;
import com.cometsrv.game.players.data.PlayerLoader;
import com.cometsrv.game.players.types.PlayerStatistics;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.user.profile.LoadProfileMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class GetProfileMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        PlayerData data = userId == client.getPlayer().getId() ? client.getPlayer().getData() : null;
        PlayerStatistics stats = data != null ? client.getPlayer().getStats() : null;

        if(data == null) {
            if(Comet.getServer().getNetwork().getSessions().getByPlayerId(userId) != null) {
                data = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId).getPlayer().getData();
                stats = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId).getPlayer().getStats();
            }
        }

        if(data == null) {
            data = PlayerLoader.loadDataById(userId);
            stats = PlayerLoader.loadStatistics(userId);
        }

        if(data == null) {
            return;
        }

        client.send(LoadProfileMessageComposer.compose(data, stats));
    }
}
