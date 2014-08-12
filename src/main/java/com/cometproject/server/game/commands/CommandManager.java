package com.cometproject.server.game.commands;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.notifications.NotificationManager;
import com.cometproject.server.game.commands.staff.*;
import com.cometproject.server.game.commands.user.*;
import com.cometproject.server.game.commands.vip.*;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Map;

public class CommandManager {

    private NotificationManager notifications;

    private FastMap<String, ChatCommand> commands;

    /**
     * Initialize the commands map and load all commands
     */
    public CommandManager() {
        this.commands = new FastMap<>();
        this.notifications = new NotificationManager();

        this.loadUserCommands();
        this.loadStaffCommands();

        Logger.getLogger(CommandManager.class.getName()).info("Loaded " + commands.size() + " chat commands");
    }

    /**
     * Loads all user commands
     */
    public void loadUserCommands() {
        this.commands.put(Locale.get("command.about.name"), new AboutCommand());
        this.commands.put(Locale.get("command.build.name"), new BuildCommand());
        this.commands.put(Locale.get("command.pickall.name"), new PickAllCommand());
        this.commands.put(Locale.get("command.empty.name"), new EmptyCommand());
        this.commands.put(Locale.get("command.sit.name"), new SitCommand());
        this.commands.put(Locale.get("command.lay.name"), new LayCommand());

        // VIP commands
        this.commands.put(Locale.get("command.push.name"), new PushCommand());
        this.commands.put(Locale.get("command.pull.name"), new PullCommand());
        this.commands.put(Locale.get("command.moonwalk.name"), new MoonwalkCommand());
        this.commands.put(Locale.get("command.enable.name"), new EnableCommand());
        this.commands.put(Locale.get("command.setspeed.name"), new SetSpeedCommand());
        this.commands.put(Locale.get("command.mimic.name"), new MimicCommand());
        this.commands.put(Locale.get("command.transform.name"), new TransformCommand());
        this.commands.put(Locale.get("command.noface.name"), new NoFaceCommand());
        this.commands.put(Locale.get("command.follow.name"), new FollowCommand());
    }

    /**
     * Loads all staff commands
     */
    public void loadStaffCommands() {
        this.commands.put(Locale.get("command.teleport.name"), new TeleportCommand());
        this.commands.put(Locale.get("command.massmotd.name"), new MassMotdCommand());
        this.commands.put(Locale.get("command.hotelalert.name"), new HotelAlertCommand());
        this.commands.put(Locale.get("command.invisible.name"), new InvisibleCommand());
        this.commands.put(Locale.get("command.ban.name"), new BanCommand());
        this.commands.put(Locale.get("command.kick.name"), new KickCommand());
        this.commands.put(Locale.get("command.disconnect.name"), new DisconnectCommand());
        this.commands.put(Locale.get("command.ipban.name"), new IpBanCommand());
        this.commands.put(Locale.get("command.alert.name"), new AlertCommand());
        this.commands.put(Locale.get("command.roomalert.name"), new RoomAlertCommand());
        this.commands.put(Locale.get("command.givebadge.name"), new GiveBadgeCommand());
        this.commands.put(Locale.get("command.removebadge.name"), new RemoveBadgeCommand());
        this.commands.put(Locale.get("command.roomkick.name"), new RoomKickCommand());
        this.commands.put(Locale.get("command.coins.name"), new CoinsCommand());
        this.commands.put(Locale.get("command.points.name"), new PointsCommand());
        this.commands.put(Locale.get("command.unload.name"), new UnloadCommand());
        this.commands.put(Locale.get("command.roommute.name"), new RoomMuteCommand());
        this.commands.put(Locale.get("command.reload.name"), new ReloadCommand());
        this.commands.put(Locale.get("command.maintenance.name"), new MaintenanceCommand());
    }

    /**
     * Checks whether the request is a valid command alias
     *
     * @param message The requested command alias
     * @return The result of the check
     */
    public boolean isCommand(String message) {
        String executor = message.split(" ")[0];

        return executor.equals(Locale.get("command.commands.name")) || commands.containsKey(executor);
    }

    /**
     * Attempts to execute the given command
     *
     * @param message The alias of the command and the parameters
     * @param client  The player who is attempting to execute the command
     * @throws Exception
     */
    public void parse(String message, Session client) throws Exception {
        String executor = message.split(" ")[0];
        String commandName = executor.equals(Locale.get("command.commands.name")) ? Locale.get("command.commands.name") : this.commands.get(executor).getPermission();

        if (executor.equals(Locale.get("command.commands.name"))) {
            StringBuilder list = new StringBuilder();

            for (Map.Entry<String, ChatCommand> command : this.commands.entrySet()) {
                if (client.getPlayer().getPermissions().hasCommand(command.getValue().getPermission()))
                    list.append(":" + command.getKey() + " - " + command.getValue().getDescription() + "\n");
            }

            client.send(MotdNotificationComposer.compose(Locale.get("command.commands.title") + " - Comet " + Comet.getBuild() + "\n================================================\n" + list.toString()));
            return;
        }

        if (client.getPlayer().getPermissions().hasCommand(commandName)) {
            this.commands.get(executor).execute(client, getParams(message.split(" ")));
            CometManager.getLogger().info(client.getPlayer().getData().getUsername() + " executed command: :" + message);
        } else {
            if (CometManager.getPermissions().getCommands().containsKey(commandName) &&
                    CometManager.getPermissions().getCommands().get(commandName).isVipOnly() &&
                    !client.getPlayer().getData().isVip())
                ChatCommand.sendChat(Locale.get("command.vip"), client);

            CometManager.getLogger().info(client.getPlayer().getData().getUsername() + " tried executing command: :" + message);
        }
    }

    /**
     * Gets the parameters from the command that was executed (removing the first record of this array)
     *
     * @param splitStr The executed command, split by " "
     * @return The parameters for the command
     */
    private String[] getParams(String[] splitStr) {
        String[] a = new String[splitStr.length - 1];

        for (int i = 0; i < splitStr.length; i++) {
            if (i == 0) {
                continue;
            }

            a[i - 1] = splitStr[i];
        }

        return a;
    }

    public NotificationManager getNotifications() {
        return notifications;
    }
}
