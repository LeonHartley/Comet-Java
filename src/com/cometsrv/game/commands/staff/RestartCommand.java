package com.cometsrv.game.commands.staff;

import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometsrv.network.sessions.Session;

public class RestartCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        client.send(WisperMessageComposer.compose(client.getPlayer().getId(), Locale.get("command.error.disabled")));
        /*try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File currentJar = new File(Comet.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            if(!currentJar.getName().endsWith(".jar"))
                throw new Exception();

            final ArrayList<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            System.exit(0);
        } catch(Exception e) {
            client.send(MotdNotificationComposer.compose(Locale.get("command.restart.linux")));
        }*/
    }

    @Override
    public String getPermission() {
        return "restart_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.restart.description");
    }
}
