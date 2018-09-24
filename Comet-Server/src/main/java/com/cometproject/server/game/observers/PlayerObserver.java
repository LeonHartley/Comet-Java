package com.cometproject.server.game.observers;

import com.cometproject.server.game.players.types.Player;

import java.util.Observable;
import java.util.Observer;

public class PlayerObserver implements Observer {

    @Override
    public void update(Observable observable, Object arg)
    {
        Player player = (Player) observable;

        if(player == null)
            return;

        player.saveJsonObject();
    }

}
