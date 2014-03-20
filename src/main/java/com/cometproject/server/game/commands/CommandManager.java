package com.cometproject.server.game.commands;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.commands.staff.*;
import com.cometproject.server.game.commands.user.*;
import com.cometproject.server.game.commands.vip.EnableCommand;
import com.cometproject.server.game.commands.vip.MoonwalkCommand;
import com.cometproject.server.game.commands.vip.PushCommand;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastMap;

import java.util.Map;

public class CommandManager {

    private FastMap<String, ChatCommand> commands;

    public CommandManager() {
        commands = new FastMap<>();

        this.loadUserCommands();
        this.loadStaffCommands();
    }

    public void loadUserCommands() {
        this.commands.put(Locale.get("command.about.name"), new AboutCommand());
        this.commands.put(Locale.get("command.build.name"), new BuildCommand());
        this.commands.put(Locale.get("command.pickall.name"), new PickAllCommand());
        this.commands.put(Locale.get("command.sellroom.name"), new SellRoomCommand());
        this.commands.put(Locale.get("command.buyroom.name"), new BuyRoomCommand());
        this.commands.put(Locale.get("command.push.name"), new PushCommand());
        this.commands.put(Locale.get("command.moonwalk.name"), new MoonwalkCommand());
        this.commands.put(Locale.get("command.enable.name"), new EnableCommand());
        this.commands.put(Locale.get("command.empty.name"), new EmptyCommand());
    }

    public void loadStaffCommands() {
        this.commands.put(Locale.get("command.restart.name"), new RestartCommand());
        this.commands.put(Locale.get("command.reload_config.name"), new ReloadConfigCommand());
        this.commands.put(Locale.get("command.teleport.name"),  new TeleportCommand());
        this.commands.put(Locale.get("command.massmotd.name"), new MassMotdCommand());
        this.commands.put(Locale.get("command.hotelalert.name"), new HotelAlertCommand());
        this.commands.put(Locale.get("command.invisible.name"), new InvisibleCommand());
        this.commands.put(Locale.get("command.force_gc.name"), new ForceGCCommand());
        this.commands.put(Locale.get("command.reload_permissions.name"), new ReloadPermissionsCommand());
        this.commands.put(Locale.get("command.ban.name"), new BanCommand());
        this.commands.put(Locale.get("command.kick.name"), new KickCommand());
        this.commands.put(Locale.get("command.disconnect.name"), new DisconnectCommand());
        this.commands.put(Locale.get("command.ipban.name"), new IpBanCommand());
    }

    public boolean isCommand(String message) {
        String executor = message.split(" ")[0];

        return executor.equals(Locale.get("command.commands.name")) || commands.containsKey(executor);
    }

    public void parse(String message, Session client) throws Exception {
        String executor = message.split(" ")[0];
        String commandName = executor.equals(Locale.get("command.commands.name")) ? Locale.get("command.commands.name") : this.commands.get(executor).getPermission();

        if(executor.equals(Locale.get("command.commands.name"))) {
            StringBuilder list = new StringBuilder();

            for(Map.Entry<String, ChatCommand> command : this.commands.entrySet()) {
                if(client.getPlayer().getPermissions().hasCommand(command.getValue().getPermission()))
                    list.append(":" + command.getKey() + " - " + command.getValue().getDescription() + "\n");
            }

            client.send(AdvancedAlertMessageComposer.compose(Locale.get("command.commands.title"), list.toString()));
            return;
        }

        if(client.getPlayer().getPermissions().hasCommand(commandName)) {
            this.commands.get(executor).execute(client, getParams(message.split(" ")));
            GameEngine.getLogger().info(client.getPlayer().getData().getUsername() + " executed command: :" + message);
        } else {
            GameEngine.getLogger().info(client.getPlayer().getData().getUsername() + " tried executing command: :" + message);
        }
    }

    private String[] getParams(String[] splitStr) {
        String[] a = new String[splitStr.length - 1];

        for(int i = 0; i < splitStr.length; i++) {
            if(i == 0) {
                continue;
            }

            a[i - 1] = splitStr[i];
        }

        return a;
    }
}
