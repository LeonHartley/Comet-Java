package com.cometproject.server.game.rooms.objects.entities.types.data;

import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.rooms.objects.misc.Position;
import org.apache.log4j.Logger;


public class PlayerBotData extends BotData {
    private Logger log = Logger.getLogger(PlayerBotData.class.getName());

    private Position position;

    public PlayerBotData(int botId, String username, String motto, String figure, String gender, String ownerName, int ownerId, String messages, boolean automaticChat, int chatDelay, String botType, String botMode) {
        super(botId, username, motto, figure, gender, ownerName, ownerId, messages, automaticChat, chatDelay, botType, botMode);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
