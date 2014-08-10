package com.cometproject.server.game.commands.notifications;

import javolution.util.FastMap;

import java.util.Map;

public class NotificationManager {
    private Map<String, Notification> notifications = new FastMap<>();

    public boolean isNotificationExecutor(String text) {
        return this.notifications.containsKey(text);
    }
}
