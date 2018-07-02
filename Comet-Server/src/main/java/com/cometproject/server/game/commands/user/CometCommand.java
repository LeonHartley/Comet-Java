package com.cometproject.server.game.commands.user;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.boot.CometServer;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class CometCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {
        client.send(new AlertMessageComposer("Powered by Comet Server. <br><br><b>Waves to:</b><br>- Leon<br>- Eden<br>- Matty<br>- Matou19<br>- Metus<br>- Mandoe<br>- Markones<br>- Helpi<br>- Vaguinho<br>- Equipe Staff<br>- Gladius<br>- Johno<br>- Sledmore<br>- Scott<br>- Nillus<br>- Jordan<br>- Burak<br>- Quackster<br>- Jaxter<br>- Kai<br>- lElectrico<br>- Caffeine<br>- More Caffeine<br>- Mary Jane<br><br><b>Fuckings to:</b><br>- Fahd<br>- Magrao<br>- TheGeneral<br>- Luck<br><br>Server Version: <b>" + Comet.getBuild() + "</b><br>Client Version: <b>" + CometServer.CLIENT_VERSION.replace(" (FINAL RELEASE, Thank you Leon for everything.)<br>PRODUCTION-", "").split(" ")[0] + "</b>"));
    }

    @Override
    public String getPermission() {
        return "dev";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
