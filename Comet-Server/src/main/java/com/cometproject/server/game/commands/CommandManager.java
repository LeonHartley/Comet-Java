package com.cometproject.server.game.commands;

import com.cometproject.api.commands.CommandInfo;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.development.*;
import com.cometproject.server.game.commands.gimmicks.HugCommand;
import com.cometproject.server.game.commands.gimmicks.KissCommand;
import com.cometproject.server.game.commands.gimmicks.PunchCommand;
import com.cometproject.server.game.commands.gimmicks.RobCommand;
import com.cometproject.server.game.commands.notifications.NotificationManager;
import com.cometproject.server.game.commands.staff.*;
import com.cometproject.server.game.commands.staff.alerts.*;
import com.cometproject.server.game.commands.staff.banning.*;
import com.cometproject.server.game.commands.staff.bundles.BundleCommand;
import com.cometproject.server.game.commands.staff.bundles.CloneRoomCommand;
import com.cometproject.server.game.commands.staff.cache.ReloadCommand;
import com.cometproject.server.game.commands.staff.cache.ReloadGroupCommand;
import com.cometproject.server.game.commands.staff.fun.RollCommand;
import com.cometproject.server.game.commands.staff.muting.MuteCommand;
import com.cometproject.server.game.commands.staff.muting.RoomMuteCommand;
import com.cometproject.server.game.commands.staff.muting.UnmuteCommand;
import com.cometproject.server.game.commands.staff.rewards.*;
import com.cometproject.server.game.commands.staff.rewards.mass.*;
import com.cometproject.server.game.commands.user.*;
import com.cometproject.server.game.commands.user.group.DeleteGroupCommand;
import com.cometproject.server.game.commands.user.group.EjectAllCommand;
import com.cometproject.server.game.commands.user.muting.MuteBotsCommand;
import com.cometproject.server.game.commands.user.muting.MutePetsCommand;
import com.cometproject.server.game.commands.user.room.*;
import com.cometproject.server.game.commands.user.settings.*;
import com.cometproject.server.game.commands.user.ws.RoomVideoCommand;
import com.cometproject.server.game.commands.vip.*;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.CommandLogEntry;
import com.cometproject.server.modules.ModuleManager;
import com.cometproject.server.network.messages.outgoing.messenger.InstantChatMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CommandManager implements Initialisable {
    private static CommandManager commandManagerInstance;
    private static Logger log = Logger.getLogger(CommandManager.class.getName());

    private NotificationManager notifications;
    private Map<String, ChatCommand> commands;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    /**
     * Initialize the commands map and load all commands
     */
    private CommandManager() {

    }

    public static CommandManager getInstance() {
        if (commandManagerInstance == null) {
            commandManagerInstance = new CommandManager();
        }

        return commandManagerInstance;
    }

    @Override
    public void initialize() {
        this.commands = new HashMap<>();

        this.reloadAllCommands();
        log.info("Loaded " + commands.size() + " chat commands");

        this.notifications = new NotificationManager();
        log.info("CommandManager initialized");
    }

    public void reloadAllCommands() {
        this.commands.clear();

        this.loadUserCommands();
        this.loadStaffCommands();

        if (Comet.isDebugging) {
            this.addCommand("reloadmapping", new ReloadMappingCommand());
            this.addCommand("instancestats", new InstanceStatsCommand());
            this.addCommand("roomgrid", new RoomGridCommand());
            this.addCommand("processtimes", new ProcessTimesCommand());
            this.addCommand("pos", new PositionCommand(true));
            this.addCommand("itemdata", new ItemDataCommand());
        }

        this.addCommand("itemid", new ItemVirtualIdCommand());
        this.addCommand("comet", new CometCommand());
    }

    /**
     * Loads all user commands
     */
    private void loadUserCommands() {
        this.addCommand(Locale.get("command.commands.name"), new CommandsCommand());
        this.addCommand(Locale.get("command.about.name"), new AboutCommand());
        this.addCommand(Locale.get("command.pickall.name"), new PickAllCommand());
        this.addCommand(Locale.get("command.ejectall.name"), new EjectAllCommand());
        this.addCommand(Locale.get("command.empty.name"), new EmptyCommand());
        this.addCommand(Locale.get("command.sit.name"), new SitCommand());
        this.addCommand(Locale.get("command.lay.name"), new LayCommand());
        this.addCommand(Locale.get("command.home.name"), new HomeCommand());
        this.addCommand(Locale.get("command.setmax.name"), new SetMaxCommand());
        this.addCommand(Locale.get("command.position.name"), new PositionCommand());
        this.addCommand(Locale.get("command.deletegroup.name"), new DeleteGroupCommand());
        this.addCommand(Locale.get("command.togglefriends.name"), new ToggleFriendsCommand());
        this.addCommand(Locale.get("command.enablecommand.name"), new EnableCommand());
        this.addCommand(Locale.get("command.disablecommand.name"), new DisableCommand());
        this.addCommand("screenshot", new ScreenshotCommand());
        this.addCommand(Locale.get("command.colour.name"), new ColourCommand());
        this.addCommand(Locale.get("command.flagme.name"), new FlagMeCommand());
        this.addCommand(Locale.get("command.flaguser.name"), new FlagUserCommand());
        this.addCommand(Locale.get("command.randomize.name"), new RandomizeCommand());
        this.addCommand(Locale.get("command.emptypets.name"), new EmptyPetsCommand());
        this.addCommand(Locale.get("command.emptybots.name"), new EmptyBotsCommand());
        this.addCommand(Locale.get("command.mutebots.name"), new MuteBotsCommand());
        this.addCommand(Locale.get("command.mutepets.name"), new MutePetsCommand());
        this.addCommand(Locale.get("command.toggleevents.name"), new ToggleEventsCommand());
        this.addCommand(Locale.get("command.emptyfriends.name"), new EmptyFriendsCommand());
        this.addCommand(Locale.get("command.reward.name"), new RewardCommand());
        this.addCommand(Locale.get("command.height.name"), new HeightCommand());
        this.addCommand(Locale.get("command.personalstaff.name"), new PersonalStaffCommand());


        // VIP commands
        this.addCommand(Locale.get("command.push.name"), new PushCommand());
        this.addCommand(Locale.get("command.pull.name"), new PullCommand());
        this.addCommand(Locale.get("command.moonwalk.name"), new MoonwalkCommand());
        this.addCommand(Locale.get("command.enable.name"), new EffectCommand());
        this.addCommand(Locale.get("command.setspeed.name"), new SetSpeedCommand());
        this.addCommand(Locale.get("command.mimic.name"), new MimicCommand());
        this.addCommand(Locale.get("command.transform.name"), new TransformCommand());
        this.addCommand(Locale.get("command.noface.name"), new NoFaceCommand());
        this.addCommand(Locale.get("command.follow.name"), new FollowCommand());
        this.addCommand(Locale.get("command.superpull.name"), new SuperPullCommand());
        this.addCommand(Locale.get("command.redeemcredits.name"), new RedeemCreditsCommand());
        this.addCommand(Locale.get("command.handitem.name"), new HandItemCommand());
        this.addCommand(Locale.get("command.togglediagonal.name"), new ToggleDiagonalCommand());
        this.addCommand(Locale.get("command.fastwalk.name"), new FastWalkCommand());
        this.addCommand(Locale.get("command.hidewired.name"), new HideWiredCommand());
        this.addCommand(Locale.get("command.roomvideo.name"), new RoomVideoCommand());
        this.addCommand(Locale.get("command.disablewhisper.name"), new DisableWhisperCommand());
        this.addCommand(Locale.get("command.namecolour.name"), new NameColourCommand());

        // Gimmick commands
        this.addCommand(Locale.get("command.rob.name"), new RobCommand());
        this.addCommand(Locale.get("command.kiss.name"), new KissCommand());
        this.addCommand(Locale.get("command.hug.name"), new HugCommand());
        this.addCommand(Locale.get("command.punch.name"), new PunchCommand());
    }

    /**
     * Loads all staff commands
     */
    private void loadStaffCommands() {
        this.addCommand(Locale.get("command.teleport.name"), new TeleportCommand());
        this.addCommand(Locale.get("command.massmotd.name"), new MassMotdCommand());
        this.addCommand(Locale.get("command.hotelalert.name"), new HotelAlertCommand());
        this.addCommand(Locale.get("command.invisible.name"), new InvisibleCommand());
        this.addCommand(Locale.get("command.superban.name"), new SuperBanCommand());
        this.addCommand(Locale.get("command.ban.name"), new BanCommand());
        this.addCommand(Locale.get("command.unban.name"), new UnBanCommand());
        this.addCommand(Locale.get("command.kick.name"), new KickCommand());
        this.addCommand(Locale.get("command.disconnect.name"), new DisconnectCommand());
        this.addCommand(Locale.get("command.ipban.name"), new IpBanCommand());
        this.addCommand(Locale.get("command.alert.name"), new AlertCommand());
        this.addCommand(Locale.get("command.roomalert.name"), new RoomAlertCommand());
        this.addCommand(Locale.get("command.givebadge.name"), new GiveBadgeCommand());
        this.addCommand(Locale.get("command.removebadge.name"), new RemoveBadgeCommand());
        this.addCommand(Locale.get("command.roomkick.name"), new RoomKickCommand());
        this.addCommand(Locale.get("command.coins.name"), new CoinsCommand());
        this.addCommand(Locale.get("command.points.name"), new PointsCommand());
        this.addCommand(Locale.get("command.duckets.name"), new DucketsCommand());
        this.addCommand(Locale.get("command.unload.name"), new UnloadCommand());
        this.addCommand(Locale.get("command.roommute.name"), new RoomMuteCommand());
        this.addCommand(Locale.get("command.reload.name"), new ReloadCommand());
        this.addCommand(Locale.get("command.maintenance.name"), new MaintenanceCommand());
        this.addCommand(Locale.get("command.roomaction.name"), new RoomActionCommand());
        this.addCommand(Locale.get("command.eventalert.name"), new EventAlertCommand());
        this.addCommand(Locale.get("command.machineban.name"), new MachineBanCommand());
        this.addCommand(Locale.get("command.makesay.name"), new MakeSayCommand());
        this.addCommand(Locale.get("command.mute.name"), new MuteCommand());
        this.addCommand(Locale.get("command.unmute.name"), new UnmuteCommand());
        this.addCommand(Locale.get("command.masscoins.name"), new MassCoinsCommand());
        this.addCommand(Locale.get("command.massbadge.name"), new MassBadgeCommand());
        this.addCommand(Locale.get("command.massduckets.name"), new MassDucketsCommand());
        this.addCommand(Locale.get("command.masspoints.name"), new MassPointsCommand());
        this.addCommand(Locale.get("command.mass.seasonal.name"), new MassSeasonalCommand());
        this.addCommand(Locale.get("command.playerinfo.name"), new PlayerInfoCommand());
        this.addCommand(Locale.get("command.roombadge.name"), new RoomBadgeCommand());
        this.addCommand(Locale.get("command.shutdown.name"), new ShutdownCommand());
        this.addCommand(Locale.get("command.summon.name"), new SummonCommand());
        this.addCommand(Locale.get("command.hotelalertlink.name"), new HotelAlertLinkCommand());
        this.addCommand(Locale.get("command.gotoroom.name"), new GotoRoomCommand());
        this.addCommand(Locale.get("command.notification.name"), new NotificationCommand());
        this.addCommand(Locale.get("command.quickpoll.name"), new QuickPollCommand());
        this.addCommand(Locale.get("command.roomoption.name"), new RoomOptionCommand());
        this.addCommand(Locale.get("command.massfreeze.name"), new MassFreezeCommand());
        this.addCommand(Locale.get("command.massteleport.name"), new MassTeleportCommand());
        this.addCommand(Locale.get("command.listen.name"), new ListenCommand());
        this.addCommand(Locale.get("command.staffalert.name"), new StaffAlertCommand());
        this.addCommand(Locale.get("command.staffinfo.name"), new StaffInfoCommand());
        this.addCommand(Locale.get("command.roomnotification.name"), new RoomNotificationCommand());
        this.addCommand(Locale.get("command.hotelvideo.name"), new HotelVideoCommand());

        // New
        this.addCommand(Locale.get("command.advban.name"), new AdvBanCommand());
        this.addCommand(Locale.get("command.softban.name"), new SoftBanCommand());
        this.addCommand(Locale.get("command.masseffect.name"), new MassEffectCommand());
        this.addCommand(Locale.get("command.masshanditem.name"), new MassHandItemCommand());
        this.addCommand(Locale.get("command.freeze.name"), new FreezeCommand());
        this.addCommand(Locale.get("command.unfreeze.name"), new UnfreezeCommand());
        this.addCommand(Locale.get("command.eventreward.name"), new EventRewardCommand());
        this.addCommand(Locale.get("command.eventwon.name"), new EventWonCommand());
        this.addCommand(Locale.get("command.viewinventory.name"), new ViewInventoryCommand());

        // Room bundles
        this.addCommand(Locale.get("command.bundle.name"), new BundleCommand());
        this.addCommand(Locale.get("command.cloneroom.name"), new CloneRoomCommand());

        // Cache
        this.addCommand(Locale.get("command.reloadgroup.name"), new ReloadGroupCommand());

        // Fun
        this.addCommand(Locale.get("command.roll.name"), new RollCommand());
    }

    /**
     * Checks whether the request is a valid command alias
     *
     * @param message The requested command alias
     * @return The result of the check
     */
    public boolean isCommand(String message) {
        if (message.length() <= 1) return false;

        if (message.equals(" ")) {
            return false;
        }

        if (message.startsWith(" ")) return false;

        String executor = message.split(" ")[0].toLowerCase();

        if (executor.startsWith(" ")) {
            executor = executor.substring(1);
        }

        boolean isCommand = executor.equals(":" + Locale.get("command.commands.name")) || commands.containsKey(executor.substring(1)) || ModuleManager.getInstance().getEventHandler().getCommands().containsKey(executor);

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
     */
    public boolean parse(String message, Session client) {
        String executor = message.split(" ")[0].toLowerCase();

        final ChatCommand chatCommand = this.get(executor);

        if (message.startsWith(" "))
            return false;

        final CommandInfo moduleCommandInfo = ModuleManager.getInstance().getEventHandler().getCommands().get(executor);

        String commandName = chatCommand == null ? (moduleCommandInfo != null ? moduleCommandInfo.getPermission() : null) : chatCommand.getPermission();

        if (commandName == null) {
            return false;
        }

        if (client.getPlayer().getPermissions().hasCommand(commandName) || commandName.equals("")) {
            if (client.getPlayer().getEntity().getRoom().getData().getDisabledCommands().contains(executor)) {
                ChatCommand.sendNotif(Locale.get("command.disabled"), client);
                return true;
            }

            final String[] params = getParams(message.split(" "));

            if (chatCommand == null) {
                ModuleManager.getInstance().getEventHandler().handleCommand(client, executor, params);
            } else {
                if (chatCommand.isAsync()) {
                    this.executorService.submit(new ChatCommand.Execution(chatCommand, params, client));
                } else {
                    chatCommand.execute(client, params);
                }
            }

            try {
                if (LogManager.ENABLED) {
                    LogManager.getInstance().getStore().getLogEntryContainer().put(new CommandLogEntry(client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getId(), message));
                    if (chatCommand != null && client.getPlayer().getData().getRank() >= Integer.parseInt(Locale.getOrDefault("logchat.minrank", "5")) && chatCommand.isLoggable()) {
                        for (Session player : ModerationManager.getInstance().getLogChatUsers()) {
                            player.send(new InstantChatMessageComposer(chatCommand.getLoggableDescription(), Integer.MAX_VALUE - 1));
                        }
                    }
                }
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

    private void addCommand(String executor, ChatCommand command) {
        final List<String> keyList = Lists.newArrayList(executor.split(","));
        for (String key : keyList) {
            this.commands.put(":" + key, command);
        }
    }

    public NotificationManager getNotifications() {
        return notifications;
    }

    public Map<String, ChatCommand> getChatCommands() {
        return this.commands;
    }
}
