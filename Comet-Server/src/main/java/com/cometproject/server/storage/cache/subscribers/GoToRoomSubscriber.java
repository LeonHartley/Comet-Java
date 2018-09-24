package com.cometproject.server.storage.cache.subscribers;

import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class GoToRoomSubscriber implements ISubscriber {
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
        Data data = new Gson().fromJson(message, Data.class);

        if (data == null || data.getRoomId() == null || data.getRoomId() == 0 ||
                data.getUsername() == null || data.getUsername().equals(""))
            return;

        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(data.getUsername());

        if (session == null)
            return;

        session.send(new RoomForwardMessageComposer(data.getRoomId()));
    }

    @Override
    public String getChannel() {
        return "comet.goto.room";
    }

    private class Data {
        private String username;
        private Integer roomId;

        public Data(String username, Integer roomId) {
            this.username = username;
            this.roomId = roomId;
        }

        public String getUsername() {
            return username;
        }

        public Integer getRoomId() {
            return roomId;
        }
    }
}
