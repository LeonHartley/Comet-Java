package com.cometproject.server.game.players.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.types.Player;

public class SubscriptionComponent {
    private Player player;

    private boolean hasSub;
    private int expire;

    //private static Logger log = Logger.getLogger(SubscriptionComponent.class.getName());

    public SubscriptionComponent(Player player) {
        this.player = player;

        this.load();
    }

    public void load() {
        this.hasSub = true;
        this.expire = 1408980390;

        // TODO: buy HC!!
        /*
        try {
            ResultSet sub = Comet.getServer().getStorage().getRow("SELECT * FROM player_subscriptions WHERE user_id = " + getPlayer().getId() + " LIMIT 1");

            if(sub == null) {
                this.hasSub = false;
                this.expire = 0;

                return;
            }

            this.hasSub = true;
            this.expire = sub.getInt("expire");
        } catch(Exception e) {
            log.error("Error while loading user subscription", e);
        }*/
    }

    public void add(int days) {
        // TODO: Add or extend the subscription
    }

    public void delete() {
        this.hasSub = false;
        this.expire = 0;

        // TODO: update fuserights

        Comet.getServer().getStorage().execute("DELETE FROM player_subscriptions WHERE user_id = " + getPlayer().getId());
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
