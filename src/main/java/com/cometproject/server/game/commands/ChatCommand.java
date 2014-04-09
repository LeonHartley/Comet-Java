package com.cometproject.server.game.commands;

import com.cometproject.server.config.Locale;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
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

    public final void sendChat(String msg, Session c) {
        c.send(WisperMessageComposer.compose(c.getPlayer().getEntity().getVirtualId(), msg));
    }

    protected String merge(String[] params, int startIndex) {
        String word = "";

        for(int i = startIndex; i < params.length; i++) {
            if(!params[params.length - 1].equals(word))
                word += " ";
            else
                word += params[i];
        }

        return word;
    }

    protected String merge(String[] params) {
        return merge(params, 0);
    }
}
