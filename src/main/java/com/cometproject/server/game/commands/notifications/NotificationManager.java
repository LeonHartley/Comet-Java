package com.cometproject.server.game.commands.notifications;

import java.util.Map;

public class NotificationManager {
    private Map<String, Notification> notifications;

    public boolean isNotificationExecutor(String text) {
        return this.notifications.containsKey(text);
    }
}
