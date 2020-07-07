package com.cometproject.server.game.permissions.types;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CommandPermission {
    private final String commandId;
    private final int minimumRank;
    private final boolean vipOnly;
    private final boolean rightsOnly;

    public CommandPermission(String commandId, int minimumRank, boolean vipOnly, boolean rightsOnly) {
        this.commandId = commandId;
        this.minimumRank = minimumRank;
        this.vipOnly = vipOnly;
        this.rightsOnly = rightsOnly;
    }

    public String getCommandId() {
        return commandId;
    }

    public int getMinimumRank() {
        return minimumRank;
    }

    public boolean vipOnly() {
        return vipOnly;
    }

    public boolean rightsOnly() {
        return rightsOnly;
    }
}
