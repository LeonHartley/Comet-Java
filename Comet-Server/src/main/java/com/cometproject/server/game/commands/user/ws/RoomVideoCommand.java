package com.cometproject.server.game.commands.user.ws;

import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Maps;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;

public class RoomVideoCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            return;
        }

        final String url = params[0];

        // todo: verification

        final Map<String, String> res = Maps.newHashMap();

        res.put("type", "youtube");
        res.put("data", url.replace("watch?v=", "embed/") + "?autoplay=1");

        final String msg = JsonUtil.getInstance().toJson(res);

        for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
            if (playerEntity.getPlayer().getSession().getWsChannel() != null) {
                playerEntity.getPlayer().getSession().getWsChannel().writeAndFlush(new TextWebSocketFrame(msg));
            }
        }
    }

    @Override
    public String getPermission() {
        return "roomvideo_command";
    }

    @Override
    public String getParameter() {
        return "%video%";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roomvideo.description");
    }
}
