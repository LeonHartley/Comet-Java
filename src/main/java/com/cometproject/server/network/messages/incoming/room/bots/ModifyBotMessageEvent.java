package com.cometproject.server.network.messages.incoming.room.bots;

import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.entities.types.data.PlayerBotData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.RandomInteger;
import com.google.gson.Gson;

public class ModifyBotMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        PlayerEntity entity = client.getPlayer().getEntity();

        if(entity == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) {
            return;
        }

        int botId = msg.readInt();
        int action = msg.readInt();
        String data = msg.readString();

        BotEntity botEntity = room.getEntities().getEntityByBotId(botId);

        switch(action) {
            case 1:
                String figure = entity.getFigure();
                String gender = entity.getGender();

                botEntity.getData().setFigure(figure);
                botEntity.getData().setGender(gender);

                room.getEntities().broadcastMessage(UpdateInfoMessageComposer.compose(botEntity));
                break;

            case 2:
                String[] data1 = data.split(";");
                String[] messages = data1[0].split("\r");
                String automaticChat = data1[2];
                String speakingInterval = data1[4];

                if(speakingInterval.isEmpty() || Integer.parseInt(speakingInterval) < 7) {
                    speakingInterval = "7";
                }

                botEntity.getData().setMessages(messages);
                botEntity.getData().setChatDelay(Integer.parseInt(speakingInterval));
                botEntity.getData().setAutomaticChat(Boolean.parseBoolean(automaticChat));
                break;

            case 3:
                // Relax
                // Disabled in-game for some reason
                break;

            case 4:
                // Dance
                int danceId = RandomInteger.getRandom(1, 4);
                botEntity.setDanceId(danceId);

                room.getEntities().broadcastMessage(DanceMessageComposer.compose(botEntity.getVirtualId(), danceId));
                break;

            case 5:
                // Change name
                botEntity.getData().setUsername(data);

                room.getEntities().broadcastMessage(AvatarsMessageComposer.compose(botEntity));
                break;
        }

        botEntity.getData().save();
    }
}
