package com.cometproject.server.game.commands;

import com.cometproject.server.config.Locale;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.RoomNotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.sessions.Session;


public abstract class ChatCommand {
    public abstract void execute(Session client, String[] params);

    public abstract String getPermission();

    public abstract String getDescription();

    public final Composer success(String msg) {
        return AdvancedAlertMessageComposer.compose(Locale.get("command.successful"), msg);
    }

    public static void sendNotif(String msg, Session c) {
//        c.send(WisperMessageComposer.compose(c.getPlayer().getEntity().getId(), msg));
        c.send(RoomNotificationMessageComposer.bubble("generic", msg));
    }

    public final String merge(String[] params) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (String s : params) {
            if (!params[params.length - 1].equals(s))
                stringBuilder.append(s).append(" ");
            else
                stringBuilder.append(s);
        }

        return stringBuilder.toString();
    }

    public String merge(String[] params, int begin) {
        final StringBuilder mergedParams = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            if (i >= begin) {
                mergedParams.append(params[i]).append(" ");
            }
        }

        return mergedParams.toString();
    }
}
