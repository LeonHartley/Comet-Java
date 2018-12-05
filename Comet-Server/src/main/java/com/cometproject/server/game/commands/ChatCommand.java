package com.cometproject.server.game.commands;

import com.cometproject.server.config.Locale;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;


public abstract class ChatCommand {
    public static void sendNotif(String msg, Session session) {
        session.send(new NotificationMessageComposer("generic", msg));
    }

    public static void sendAlert(String msg, Session session) {
        session.send(new AlertMessageComposer(msg));
    }

    public static void sendWhisper(String msg, Session session) {
        session.send(new WhisperMessageComposer(session.getPlayer().getEntity().getId(), msg));
    }

    public static void isExecuted(Session session) {
        session.send(new NotificationMessageComposer("up", Locale.getOrDefault("command.executed", "Command is executed succesfully.")));
    }

    public abstract void execute(Session client, String[] params);

    public abstract String getPermission();

    public abstract String getParameter();

    public abstract String getDescription();

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
    
    public String getLoggableDescription() { return getDescription(); }

    public boolean isLoggable() { return false; }

    public boolean isHidden() {
        return false;
    }

    public boolean canDisable() {
        return false;
    }

    public boolean isAsync() {
        return false;
    }

    public boolean bypassFilter() {
        return false;
    }

    public static class Execution implements Runnable {
        private ChatCommand command;
        private String[] params;
        private Session session;

        public Execution(ChatCommand command, String[] params, Session session) {
            this.command = command;
            this.params = params;
            this.session = session;
        }

        @Override
        public void run() {
            command.execute(session, params);
        }
    }
}
