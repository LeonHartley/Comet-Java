package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.game.rooms.types.components.types.Trade;

import java.util.ArrayList;
import java.util.List;


public class TradeComponent {
    private List<Trade> trades;
    private final RoomInstance room;

    public TradeComponent(RoomInstance room) {
        this.room = room;
        this.trades = new ArrayList<>();
    }

    public void add(Trade trade) {
        trade.setTradeComponent(this);

        this.trades.add(trade);
    }

    public Trade get(PlayerEntity client) {
        for (Trade trade : this.getTrades()) {
            if (trade.getUser1() == client || trade.getUser2() == client)
                return trade;
        }

        return null;
    }

    public void remove(Trade trade) {
        this.trades.remove(trade);
    }

    public synchronized List<Trade> getTrades() {
        return this.trades;
    }

    public RoomInstance getRoom() {
        return room;
    }
}
