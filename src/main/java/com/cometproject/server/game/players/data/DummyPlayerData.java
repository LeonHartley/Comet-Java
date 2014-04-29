package com.cometproject.server.game.players.data;

/**
 * Created by Matty on 29/04/2014.
 */
public class DummyPlayerData extends PlayerData {
    public DummyPlayerData() {
        super(-1, "Unknown Player", "Contact administrator asap", "", "F", -1, 0, 0, "", 0, false, 0);
    }

    @Override
    public boolean save() {
        return false;
    }
}
