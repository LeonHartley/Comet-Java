package com.cometproject.server.game.rooms.types.components.bots.server;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.bots.Bot;

public class ServerBot extends Bot{

    public ServerBot(String figure, String motto, Room room) {
        super(-1337, -1337, "Comet", "Server", "", "M", motto, -1337, room.getModel().getDoorX(), room.getModel().getDoorY(), room.getModel().getDoorZ(), room);
    }

    @Override
    public void tick() {

    }
}
