package com.cometproject.server.network.messages.incoming.user.wardrobe;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class ChangeLooksMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String gender = msg.readString();
        String figure = msg.readString();

        // TODO: Check validity of the figure.

        int timeSinceLastUpdate = ((int) Comet.getTime()) - client.getPlayer().getLastFigureUpdate();

        if(timeSinceLastUpdate >= CometSettings.playerFigureUpdateTimeout) {
            client.getPlayer().getData().setGender(gender);
            client.getPlayer().getData().setFigure(figure);
            client.getPlayer().getData().save();

            client.getPlayer().poof();
            client.getPlayer().setLastFigureUpdate((int) Comet.getTime());
        }
    }
}
