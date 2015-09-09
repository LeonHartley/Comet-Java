package com.cometproject.server.game.commands;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.development.*;
import com.cometproject.server.game.commands.gimmicks.KissCommand;
import com.cometproject.server.game.commands.gimmicks.PunchCommand;
import com.cometproject.server.game.commands.gimmicks.SexCommand;
import com.cometproject.server.game.commands.gimmicks.SlapCommand;
import com.cometproject.server.game.commands.notifications.NotificationManager;
import com.cometproject.server.game.commands.staff.*;
import com.cometproject.server.game.commands.staff.alerts.*;
import com.cometproject.server.game.commands.staff.banning.BanCommand;
import com.cometproject.server.game.commands.staff.banning.IpBanCommand;
import com.cometproject.server.game.commands.staff.banning.MachineBanCommand;
import com.cometproject.server.game.commands.staff.cache.ReloadCommand;
import com.cometproject.server.game.commands.staff.cache.ReloadGroupCommand;
import com.cometproject.server.game.commands.staff.fun.RollCommand;
import com.cometproject.server.game.commands.staff.muting.MuteCommand;
import com.cometproject.server.game.commands.staff.muting.RoomMuteCommand;
import com.cometproject.server.game.commands.staff.muting.UnmuteCommand;
import com.cometproject.server.game.commands.staff.rewards.*;
import com.cometproject.server.game.commands.staff.rewards.mass.MassBadgeCommand;
import com.cometproject.server.game.commands.staff.rewards.mass.MassCoinsCommand;
import com.cometproject.server.game.commands.staff.rewards.mass.MassDucketsCommand;
import com.cometproject.server.game.commands.staff.rewards.mass.MassPointsCommand;
import com.cometproject.server.game.commands.user.*;
import com.cometproject.server.game.commands.user.group.DeleteGroupCommand;
import com.cometproject.server.game.commands.user.room.PickAllCommand;
import com.cometproject.server.game.commands.user.room.SetMaxCommand;
import com.cometproject.server.game.commands.user.room.SetSpeedCommand;
import com.cometproject.server.game.commands.user.settings.DisableCommand;
import com.cometproject.server.game.commands.user.settings.EnableCommand;
import com.cometproject.server.game.commands.user.settings.ToggleFriendsCommand;
import com.cometproject.server.game.commands.vip.*;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.CommandLogEntry;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.Initializable;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CommandManager implements Initializable {
    private static CommandManager commandManagerInstance;
    private static Logger log = Logger.getLogger(CommandManager.class.getName());

    private NotificationManager notifications;
    private Map<String, ChatCommand> commands;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    /**
     * Initialize the commands map and load all commands
     */
    public CommandManager() {

    }

    @Override
    public void initialize() {
        this.commands = new HashMap<>();

        this.reloadAllCommands();
        log.info("Loaded " + commands.size() + " chat commands");

        this.notifications = new NotificationManager();
        log.info("CommandManager initialized");
    }

    public static CommandManager getInstance() {
        if (commandManagerInstance == null) {
            commandManagerInstance = new CommandManager();
        }

        return commandManagerInstance;
    }

    public void reloadAllCommands() {
        this.commands.clear();

        this.loadUserCommands();
        this.loadStaffCommands();

        if (Comet.isDebugging) {
            this.commands.put("reloadmapping", new ReloadMappingCommand());
        }

        this.commands.put("instancestats", new InstanceStatsCommand());
        this.commands.put("roomgrid", new RoomGridCommand());
        this.commands.put("processtimes", new ProcessTimesCommand());
        this.commands.put("markisafaggot", new FastProcessCommand());
    }

    /**
     * Loads all user commands
     */
    public void loadUserCommands() {
        this.commands.put(Locale.get("command.commands.name"), new CommandsCommand());
        this.commands.put(Locale.get("command.about.name"), new AboutCommand());
        this.commands.put(Locale.get("command.build.name"), new BuildCommand());
        this.commands.put(Locale.get("command.pickall.name"), new PickAllCommand());
        this.commands.put(Locale.get("command.empty.name"), new EmptyCommand());
        this.commands.put(Locale.get("command.sit.name"), new SitCommand());
        this.commands.put(Locale.get("command.lay.name"), new LayCommand());
        this.commands.put(Locale.get("command.home.name"), new HomeCommand());
        this.commands.put(Locale.get("command.setmax.name"), new SetMaxCommand());
        this.commands.put(Locale.get("command.position.name"), new PositionCommand());
        this.commands.put(Locale.get("command.deletegroup.name"), new DeleteGroupCommand());
        this.commands.put(Locale.get("command.togglefriends.name"), new ToggleFriendsCommand());
        this.commands.put(Locale.get("command.enablecommand.name"), new EnableCommand());
        this.commands.put(Locale.get("command.disablecommand.name"), new DisableCommand());
        this.commands.put("screenshot", new ScreenshotCommand());

        // VIP commands
        this.commands.put(Locale.get("command.push.name"), new PushCommand());
        this.commands.put(Locale.get("command.pull.name"), new PullCommand());
        this.commands.put(Locale.get("command.moonwalk.name"), new MoonwalkCommand());
        this.commands.put(Locale.get("command.enable.name"), new EffectCommand());
        this.commands.put(Locale.get("command.setspeed.name"), new SetSpeedCommand());
        this.commands.put(Locale.get("command.mimic.name"), new MimicCommand());
        this.commands.put(Locale.get("command.transform.name"), new TransformCommand());
        this.commands.put(Locale.get("command.noface.name"), new NoFaceCommand());
        this.commands.put(Locale.get("command.follow.name"), new FollowCommand());
        this.commands.put(Locale.get("command.superpull.name"), new SuperPullCommand());
        this.commands.put(Locale.get("command.redeemcredits.name"), new RedeemCreditsCommand());
        this.commands.put(Locale.get("command.handitem.name"), new HandItemCommand());
        this.commands.put(Locale.get("command.togglediagonal.name"), new ToggleDiagonalCommand());

        // Gimmick commands
        this.commands.put(Locale.get("command.slap.name"), new SlapCommand());
        this.commands.put(Locale.get("command.kiss.name"), new KissCommand());
        this.commands.put(Locale.get("command.sex.name"), new SexCommand());
        this.commands.put(Locale.get("command.punch.name"), new PunchCommand());
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
        this.commands.put(Locale.get("command.duckets.name"), new DucketsCommand());
        this.commands.put(Locale.get("command.unload.name"), new UnloadCommand());
        this.commands.put(Locale.get("command.roommute.name"), new RoomMuteCommand());
        this.commands.put(Locale.get("command.reload.name"), new ReloadCommand());
        this.commands.put(Locale.get("command.maintenance.name"), new MaintenanceCommand());
        this.commands.put(Locale.get("command.roomaction.name"), new RoomActionCommand());
        this.commands.put(Locale.get("command.eventalert.name"), new EventAlertCommand());
        this.commands.put(Locale.get("command.machineban.name"), new MachineBanCommand());
        this.commands.put(Locale.get("command.makesay.name"), new MakeSayCommand());
        this.commands.put(Locale.get("command.mute.name"), new MuteCommand());
        this.commands.put(Locale.get("command.unmute.name"), new UnmuteCommand());
        this.commands.put(Locale.get("command.masscoins.name"), new MassCoinsCommand());
        this.commands.put(Locale.get("command.massbadge.name"), new MassBadgeCommand());
        this.commands.put(Locale.get("command.massduckets.name"), new MassDucketsCommand());
        this.commands.put(Locale.get("command.masspoints.name"), new MassPointsCommand());
        this.commands.put(Locale.get("command.playerinfo.name"), new PlayerInfoCommand());
        this.commands.put(Locale.get("command.roombadge.name"), new RoomBadgeCommand());
        this.commands.put(Locale.get("command.shutdown.name"), new ShutdownCommand());
        this.commands.put(Locale.get("command.summon.name"), new SummonCommand());
        this.commands.put(Locale.get("command.hotelalertlink.name"), new HotelAlertLinkCommand());
        this.commands.put(Locale.get("command.gotoroom.name"), new GotoRoomCommand());

        // Cache
        this.commands.put(Locale.get("command.reloadgroup.name"), new ReloadGroupCommand());

        // Fun
        this.commands.put(Locale.get("command.roll.name"), new RollCommand());
    }

    /**
     * Checks whether the request is a valid command alias
     *
     * @param message The requested command alias
     * @return The result of the check
     */
    public boolean isCommand(String message) {
        if (message.length() <= 1) return false;

        String executor = message.split(" ")[0].toLowerCase();

        boolean isCommand = executor.equals(Locale.get("command.commands.name")) || commands.containsKey(executor);

        if (!isCommand) {
            for (String keys : this.commands.keySet()) {
                final List<String> keyList = Lists.newArrayList(keys.split(","));

                if (keyList.contains(executor)) {
                    return true;
                }
            }
        }

        return isCommand;
    }

    /**
     * Attempts to execute the given command
     *
     * @param message The alias of the command and the parameters
     * @param client  The player who is attempting to execute the command
     * @throws Exception
     */
    public boolean parse(String message, Session client) throws Exception {
        String executor = message.split(" ")[0].toLowerCase();

        final ChatCommand chatCommand = this.get(executor);

        if (chatCommand == null) {
            log.debug(client.getPlayer().getData().getUsername() + " executed command: :" + message);
            return false;
        }

        String commandName = chatCommand.getPermission();

        if (client.getPlayer().getPermissions().hasCommand(commandName)) {
            if (client.getPlayer().getEntity().getRoom().getData().getDisabledCommands().contains(executor)) {
                ChatCommand.sendNotif(Locale.get("command.disabled"), client);
                return true;
            }

            final String[] params = getParams(message.split(" "));

            if (chatCommand.isAsync()) {
                this.executorService.submit(new ChatCommand.Execution(chatCommand, params, client));
            } else {
                chatCommand.execute(client, params);
            }

            try {
                if (LogManager.ENABLED)
                    LogManager.getInstance().getStore().getLogEntryContainer().put(new CommandLogEntry(client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getId(), message));
            } catch (Exception ignored) {

            }

            return true;
        } else {
            if (PermissionsManager.getInstance().getCommands().containsKey(commandName) &&
                    PermissionsManager.getInstance().getCommands().get(commandName).isVipOnly() &&
                    !client.getPlayer().getData().isVip())
                ChatCommand.sendNotif(Locale.get("command.vip"), client);

            log.debug(client.getPlayer().getData().getUsername() + " tried executing command: :" + message);
            return false;
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

    private ChatCommand get(final String executor) {
        if (this.commands.containsKey(executor))
            return this.commands.get(executor);

        for (String keys : this.commands.keySet()) {
            final List<String> keyList = Lists.newArrayList(keys.split(","));

            if (keyList.contains(executor)) {
                return this.commands.get(keys);
            }
        }

        return null;
    }

    public NotificationManager getNotifications() {
        return notifications;
    }

    public Map<String, ChatCommand> getChatCommands() {
        return this.commands;
    }
}
