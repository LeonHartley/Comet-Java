package com.cometproject.server.game.commands.user.ws;

import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.ws.messages.YouTubeVideoMessage;
import com.google.common.collect.Maps;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;

public class RoomVideoCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            return;
        }

        final String videoId = params[0];
        final String message = this.merge(params, 1);
        final PlayerAvatar avatar = client.getPlayer().getData();
        final YouTubeVideoMessage msg = new YouTubeVideoMessage(videoId, message, avatar.getUsername(), avatar.getFigure());

        client.getPlayer().getEntity().getRoom().setRoomVideo(msg);
        client.getPlayer().getEntity().getRoom().getEntities().broadcastWs(msg);
    }

    @Override
    public String getPermission() {
        return "roomvideo_command";
    }

    @Override
    public String getParameter() {
        return "%video% %msg%";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roomvideo.description");
    }
}
