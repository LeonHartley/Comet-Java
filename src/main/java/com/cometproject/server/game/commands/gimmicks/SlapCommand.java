package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class SlapCommand extends ChatCommand {
    private final static String[] objects = {
        "a large black dildo",
        "a rotten fish",
        "a large trout",
        "a big black boot",
        "the back of their hand",
        "their floppy shlong"
    };

    @Override
    public void execute(Session client, String[] params) {

    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
