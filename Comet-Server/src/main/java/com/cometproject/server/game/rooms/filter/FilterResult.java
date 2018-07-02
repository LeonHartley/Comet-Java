package com.cometproject.server.game.rooms.filter;

import com.cometproject.api.game.rooms.filter.IFilterResult;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.network.messages.outgoing.messenger.InstantChatMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class FilterResult implements IFilterResult {
    private boolean isBlocked;
    private boolean wasModified;
    private String message;

    public FilterResult(String chatMessage) {
        this.isBlocked = false;
        this.wasModified = false;
        this.message = chatMessage;
    }

    public FilterResult(boolean isBlocked, String chatMessage) {
        this.isBlocked = isBlocked;
        this.wasModified = false;
        this.message = chatMessage;
    }

    public FilterResult(String chatMessage, boolean wasModified) {
        this.isBlocked = false;
        this.wasModified = wasModified;
        this.message = chatMessage;
    }

    @Override
    public boolean isBlocked() {
        return isBlocked;
    }

    public void sendLogToStaffs(Session client, String where) {
        for (Session player : ModerationManager.getInstance().getModerators()) {
            player.send(new InstantChatMessageComposer(Locale.getOrDefault("staff.chat.filter", "The user %s has triggered the filter on %b: [%c]")
                    .replace("%s", client.getPlayer().getData().getUsername())
                    .replace("%b", where)
                    .replace("%c", this.message), Integer.MAX_VALUE));
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean wasModified() {
        return wasModified;
    }
}
