package com.cometproject.server.game.players.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.types.Player;

public class SubscriptionComponent {
    private Player player;

    private boolean hasSub;
    private int expire;

    public SubscriptionComponent(Player player) {
        this.player = player;

        this.load();
    }

    public void load() {
        this.hasSub = true;
        this.expire = (int) Comet.getTime() + 315569260;

        // TODO: Subscriptions
    }

    public void add(int days) {
        // TODO: Add or extend the subscription
    }

    public void delete() {
        this.hasSub = false;
        this.expire = 0;
    }

    public boolean isValid() {
        if (this.getExpire() <= Comet.getTime()) {
            return false;
        }

        return true;
    }

    public boolean exists() {
        return this.hasSub;
    }

    public int getExpire() {
        return this.expire;
    }

    public Player getPlayer() {
        return this.player;
    }
}
