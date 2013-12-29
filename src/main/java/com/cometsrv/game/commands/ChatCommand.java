package com.cometsrv.game.commands;

import com.cometsrv.config.Locale;
import com.cometsrv.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometsrv.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometsrv.network.messages.types.Composer;
import com.cometsrv.network.sessions.Session;

public abstract class ChatCommand {
    public abstract void execute(Session client, String[] params);
    public abstract String getPermission();
    public abstract String getDescription();

    public final Composer success(String msg) {
        return AdvancedAlertMessageComposer.compose(Locale.get("command.successful"), msg);
    }

    public final void sendChat(String msg, Session c) {
        c.send(WisperMessageComposer.compose(c.getPlayer().getId(), msg));
    }

    public final String merge(String[] params) {
        String r = "";

        for(String s : params) {
            if(! params[ params.length - 1].equals(s))
                r+= s + " ";
            else
                r+= s;
        }

        return r;
    }
}
