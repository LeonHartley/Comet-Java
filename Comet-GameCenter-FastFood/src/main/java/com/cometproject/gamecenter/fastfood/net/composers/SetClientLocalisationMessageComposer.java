package com.cometproject.gamecenter.fastfood.net.composers;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;

public class SetClientLocalisationMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return 13;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(49);
        msg.writeString("basejump.exit");
        msg.writeString("Exit");
        msg.writeString("basejump.missile");
        msg.writeString("Missile");
        msg.writeString("basejump.server.maintenance");
        msg.writeString("The game server is closing, come back later!");
        msg.writeString("basejump.site.name.hhde");
        msg.writeString("Habbo.DE");
        msg.writeString("basejump.purchase.price");
        msg.writeString("Price:");
        msg.writeString("basejump.get.ready");
        msg.writeString("Get ready...");
        msg.writeString("basejump.bigparachute.limit");
        msg.writeString("THREE large parachutes can be used per game");
        msg.writeString("basejump.site.name.hhse");
        msg.writeString("Habbo.SE");
        msg.writeString("basejump.bigparachute");
        msg.writeString("Large Parachute");
        msg.writeString("basejump.instructions.3");
        msg.writeString("Use your power-ups! There's objects, large parachutes, and super shields at your disposal, use them wiseley!");
        msg.writeString("basejump.instructions.2");
        msg.writeString("Click on the red parachute button in time so the food won't smash to the table!");
        msg.writeString("basejump.instructions.1");
        msg.writeString("When the red timer is ready click it to begin and let the food fall!");
        msg.writeString("basejump.waiting.players");
        msg.writeString("Waiting for players");
        msg.writeString("basejump.name");
        msg.writeString("Fast Food");
        msg.writeString("basejump.site.name.hhit");
        msg.writeString("Habbo.IT");
        msg.writeString("basejump.bigparachute.description");
        msg.writeString("Allows last second opening");
        msg.writeString("basejump.site.name.hhfr");
        msg.writeString("Habbo.FR");
        msg.writeString("basejump.friend.request");
        msg.writeString("Add as friend");
        msg.writeString("basejump.site.name.hhfi");
        msg.writeString("Habbo.FI");
        msg.writeString("basejump.site.name.hhus");
        msg.writeString("Habbo.COM");
        msg.writeString("basejump.missile.limit");
        msg.writeString("THREE objects can be used per game.");
        msg.writeString("basejump.purchase.confirmation.no");
        msg.writeString("Cancel");
        msg.writeString("basejump.powerup_gained");
        msg.writeString("LUCKY! YOU'VE WON");
        msg.writeString("basejump.purchase.confirmation.yes");
        msg.writeString("Buy");
        msg.writeString("basejump.shield.limit");
        msg.writeString("THREE shields can be used per game");
        msg.writeString("basejump.globalscores.description");
        msg.writeString("Puntuación media actual del Hotel");
        msg.writeString("basejump.games.played.today");
        msg.writeString("Games played today: %played%/%max%");
        msg.writeString("basejump.play");
        msg.writeString("Play");
        msg.writeString("basejump.friend.request.sent");
        msg.writeString("Friend request sent!");
        msg.writeString("basejump.instructions.title");
        msg.writeString("Be quick - the fastest wins!");
        msg.writeString("basejump.globalscores.title");
        msg.writeString("Batalla de Hotel");
        msg.writeString("basejump.go");
        msg.writeString("GO!");
        msg.writeString("basejump.winner");
        msg.writeString("WINNER!");
        msg.writeString("basejump.site.name.hhbr");
        msg.writeString("Habbo.COM.BR");
        msg.writeString("basejump.site.name.hhes");
        msg.writeString("Habbo.ES");
        msg.writeString("basejump.shield");
        msg.writeString("Shield");
        msg.writeString("basejump.purchase.confirmation");
        msg.writeString("Confirm");
        msg.writeString("basejump.missile.description");
        msg.writeString("Auto-targets and blows up your best opponent's dish.");
        msg.writeString("basejump.site.name.hhno");
        msg.writeString("Habbo.NO");
        msg.writeString("basejump.site.name.hhtr");
        msg.writeString("Habbo.TR");
        msg.writeString("basejump.scores.title");
        msg.writeString("Puntuaciones");
        msg.writeString("basejump.site.name.hhnl");
        msg.writeString("Habbo.NL");
        msg.writeString("basejump.shield.description");
        msg.writeString("Shields from enemy objects");
        msg.writeString("basejump.purchase.not_enough_credits");
        msg.writeString("Insufficient Credits");
        msg.writeString("basejump.purchase");
        msg.writeString("Buy more");
        msg.writeString("basejump.games.played.thisweek");
        msg.writeString("Games played this week: %played%/%max%");
        msg.writeString("basejump.inventory.title");
        msg.writeString("Your power ups:");
        msg.writeString("basejump.habbo.maintenance");
        msg.writeString("El Hotel se cierra, ¡vuelve m\\x00e1s tarde!");
        msg.writeString("basejump.site.name.hhdk");
        msg.writeString("Habbo.DK");
    }
}
