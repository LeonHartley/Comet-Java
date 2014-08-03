package com.cometproject.server.game.players.data;

public class DummyPlayerData extends PlayerData {
    public DummyPlayerData() {
        super(-1, "Unknown Player", "Contact administrator asap", "", "F", "", -1, 0, 0, "", 0, false, 0, 0, 0);
    }

    @Override
    public void save() {

    }
}
