package com.cometproject.server.game.commands.user.settings;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class ToggleFriendsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (client.getPlayer().getSettings().getAllowFriendRequests()) {
            client.getPlayer().getSettings().setAllowFriendRequests(false);
            sendNotif(Locale.getOrDefault("command.togglefriends.disabled", "You have disabled friend requests."), client);
        } else {
            client.getPlayer().getSettings().setAllowFriendRequests(true);
            sendNotif(Locale.getOrDefault("command.togglefriends.enabled", "You have enabled friend requests."), client);

        }

        PlayerDao.saveAllowFriendRequests(client.getPlayer().getSettings().getAllowFriendRequests(), client.getPlayer().getId());
    }

    @Override
    public String getPermission() {
        return "togglefriends_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.togglefriends.description");
    }
}
