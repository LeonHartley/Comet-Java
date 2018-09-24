package com.cometproject.server.storage.cache.subscribers;

import com.cometproject.server.game.navigator.NavigatorManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RefreshDataSubscriber implements ISubscriber {
    private Jedis jedis = null;

    @Override
    public void setJedis(JedisPool jedis) {
        this.jedis = jedis.getResource();
    }

    @Override
    public void subscribe() {
        this.jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                handleMessage(message);
            }
        }, getChannel());
    }

    @Override
    public void handleMessage(String message) {
        switch (message) {
            case "navigator":
                NavigatorManager.getInstance().loadCategories();
                NavigatorManager.getInstance().loadPublicRooms();
                NavigatorManager.getInstance().loadStaffPicks();
        }
    }

    @Override
    public String getChannel() {
        return "comet.refresh.data";
    }
}
