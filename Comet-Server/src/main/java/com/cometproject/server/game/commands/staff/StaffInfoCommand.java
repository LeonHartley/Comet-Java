package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class StaffInfoCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] message) {
        final StringBuilder staffinfo = new StringBuilder("Current online staffs:\r\r");

        for (Session player : ModerationManager.getInstance().getModerators()) {
            staffinfo.append("* Username: ")
                    .append(player.getPlayer().getData().getUsername())
                    .append(" / Room: ")
                    .append(player.getPlayer().getEntity() != null
                            && player.getPlayer().getEntity().getRoom() != null
                            ? player.getPlayer().getEntity().getRoom().getData().getName() : "none")
                    .append(" / Rank: ")
                    .append(player.getPlayer().getData().getRank())
                    .append("\r");
        }

        final MotdNotificationMessageComposer msg = new MotdNotificationMessageComposer(staffinfo.toString());

        client.send(msg);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String getPermission() {
        return "staffinfo_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.staffinfo.description");
    }
}