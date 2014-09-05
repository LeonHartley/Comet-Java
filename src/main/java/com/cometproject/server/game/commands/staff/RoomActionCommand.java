package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.avatars.effects.UserEffect;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang.StringUtils;

public class RoomActionCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(params.length < 2) {
            return;
        }

        final String action = params[0];

        switch(action) {
            case "effect":
                if(!StringUtils.isNumeric(params[1])) {
                    return;
                }

                int effectId = Integer.parseInt(params[1]);

                for(PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                    playerEntity.applyEffect(new UserEffect(effectId, 0));
                }
                break;

            case "say":
                String msg = params[1];

                for(PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                    playerEntity.getRoom().getEntities().broadcastMessage(TalkMessageComposer.compose(playerEntity.getVirtualId(), msg, CometManager.getRooms().getEmotions().getEmotion(msg), 1));
                }
                break;

            case "dance":
                if(!StringUtils.isNumeric(params[1])) {
                    return;
                }

                int danceId = Integer.parseInt(params[1]);

                for(PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                    playerEntity.setDanceId(danceId);
                    playerEntity.getRoom().getEntities().broadcastMessage(DanceMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), danceId));
                }
                break;

            case "sign":
                if(!StringUtils.isNumeric(params[1])) {
                    return;
                }

                for(PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                    playerEntity.addStatus("sign", String.valueOf(params[1]));

                    playerEntity.markDisplayingSign();
                    playerEntity.markNeedsUpdate();
                }
                break;
        }
    }

    @Override
    public String getPermission() {
        return "roomaction_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roomaction.description");
    }
}
