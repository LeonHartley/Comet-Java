package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.rooms.settings.RoomTradeState;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.trading.TradeErrorMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class BeginTradeMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (client.getPlayer().getEntity() == null)
            return;

        int userId = msg.readInt();

        if (client.getPlayer().getEntity().getRoom().getData().getTradeState() == RoomTradeState.DISABLED) {
            client.send(new TradeErrorMessageComposer(6, ""));
            return;
        }

        PlayerEntity entity = (PlayerEntity) client.getPlayer().getEntity().getRoom().getEntities().getEntity(userId);

        if (client.getPlayer().getEntity().getRoom().getData().getOwnerId() != client.getPlayer().getId() && entity.getRoom().getData().getTradeState() == RoomTradeState.OWNER_ONLY) {
            client.send(new TradeErrorMessageComposer(6, ""));
            return;
        }

        if (entity.getPlayer().getSettings() != null && !entity.getPlayer().getSettings().getAllowTrade()) {
            client.send(new TradeErrorMessageComposer(4, entity.getUsername()));
            return;
        }

        if (client.getPlayer().getEntity().hasStatus(RoomEntityStatus.TRADE) || client.getPlayer() == null || client.getPlayer().getSession() == null) {
            client.send(new TradeErrorMessageComposer(7, ""));
            return;
        }

        if (entity.hasStatus(RoomEntityStatus.TRADE) || entity.getPlayer() == null || entity.getPlayer().getSession() == null) {
            client.send(new TradeErrorMessageComposer(8, entity.getUsername()));
            return;
        }

        final long time = System.currentTimeMillis();

        if (!client.getPlayer().getPermissions().getRank().floodBypass()) {
            if (time - client.getPlayer().getLastTradeTime() < 750) {
                client.getPlayer().setLastTradeFlag(client.getPlayer().getLastTradeFlag() + 1);

                if (client.getPlayer().getLastTradeFlag() >= 4) {
                    client.getPlayer().setLastTradeFlood(time / 1000L + client.getPlayer().getPermissions().getRank().floodTime());
                    client.getPlayer().setLastTradeFlag(0);
                }
            }

            if ((time / 1000L) < client.getPlayer().getLastTradeFlood()) {
                return;
            }

            client.getPlayer().setLastTradeTime(time);
        }

        client.getPlayer().getEntity().getRoom().getTrade().add(new Trade(client.getPlayer().getEntity(), entity));
    }
}
