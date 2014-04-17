package com.cometproject.server.game;

import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.commands.CommandManager;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.pets.PetManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.avatars.misc.FilterManager;
import com.cometproject.server.game.wired.WiredManager;
import org.apache.log4j.Logger;

public class GameEngine {
    private static PermissionsManager permissionsManager;
    private static NavigatorManager navigatorManager;
    private static CatalogManager catalogManager;
    private static ItemManager itemManager;
    private static RoomManager roomManager;
    private static CommandManager commandManager;
    private static WiredManager wiredManager;
    private static BanManager banManager;
    private static ModerationManager moderationManager;
    private static GroupManager groupManager;
    private static PetManager petManager;

    public static GameThread gameThread;

    private static Logger log = Logger.getLogger(GameEngine.class.getName());

    public static void init() {
        permissionsManager = new PermissionsManager();
        itemManager = new ItemManager();
        catalogManager = new CatalogManager();
        roomManager = new RoomManager();
        navigatorManager = new NavigatorManager();
        commandManager = new CommandManager();
        wiredManager = new WiredManager();
        banManager = new BanManager();
        moderationManager = new ModerationManager();
        groupManager = new GroupManager();
        petManager = new PetManager();
    }

    public static Logger getLogger() {
        return log;
    }

    public static PermissionsManager getPermissions() {
        return permissionsManager;
    }

    public static NavigatorManager getNavigator() {
        return navigatorManager;
    }

    public static CatalogManager getCatalog() {
        return catalogManager;
    }

    public static ItemManager getItems() {
        return itemManager;
    }

    public static CommandManager getCommands() {
        return commandManager;
    }

    public static RoomManager getRooms() {
        return roomManager;
    }

    public static WiredManager getWired() {
        return wiredManager;
    }

    public static BanManager getBans() {
        return banManager;
    }

    public static ModerationManager getModeration() {
        return moderationManager;
    }

    public static GroupManager getGroups() {
        return groupManager;
    }

    public static PetManager getPets() {
        return petManager;
    }

    public static GameThread getThread() {
        return gameThread;
    }
}
