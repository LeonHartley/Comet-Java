package com.cometproject.server.game.rooms.entities.types.data;

import com.cometproject.server.game.bots.BotData;
import org.apache.log4j.Logger;

public class PlayerBotData extends BotData {
    private Logger log = Logger.getLogger(PlayerBotData.class.getName());

    public PlayerBotData(int botId, String username, String motto, String figure, String gender, String ownerName, int ownerId, String messages, boolean automaticChat, int chatDelay) {
        super(botId, username, motto, figure, gender, ownerName, ownerId, messages, automaticChat, chatDelay);
    }
}
