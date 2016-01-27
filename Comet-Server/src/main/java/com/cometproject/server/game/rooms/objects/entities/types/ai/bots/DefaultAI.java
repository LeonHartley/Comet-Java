package com.cometproject.server.game.rooms.objects.entities.types.ai.bots;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.AbstractBotAI;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.utilities.RandomInteger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DefaultAI extends AbstractBotAI {

    public DefaultAI(GenericEntity entity) {
        super(entity);
    }
}
