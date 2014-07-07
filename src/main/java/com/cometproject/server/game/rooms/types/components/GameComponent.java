package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.cometproject.server.game.rooms.types.components.games.banzai.BanzaiGame;

public class GameComponent {
    private Room room;
    private RoomGame instance;

    public GameComponent(Room room) {
        this.room = room;
    }

    public void stop() {
        if (this.instance != null) {
            this.instance.stop();
        }

        this.instance = null;
    }

    public void createNew(GameType game) {
        if (game == GameType.BANZAI) {
            this.instance = new BanzaiGame(this.room);
        } else if (game == GameType.FREEZE) {
            this.instance = null; // TODO: freeze game
        }
    }

    public RoomGame getInstance() {
        return this.instance;
    }

    public Room getRoom() {
        return this.room;
    }
}
