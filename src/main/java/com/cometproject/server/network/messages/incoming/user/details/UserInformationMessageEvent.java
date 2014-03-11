package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.LoadFriendsMessageComposer;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UserInfoMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.WelcomeUserMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.permissions.AllowancesMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.CurrenciesMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastMap;

import java.util.Map;

public class UserInformationMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(UserInfoMessageComposer.compose(client.getPlayer()));

        client.send(SendCreditsMessageComposer.compose(client.getPlayer().getData().getCredits()));

        Map<Integer, Integer> currencies = new FastMap<>();

        currencies.put(0, 0); // TODO: duckets
        currencies.put(105, client.getPlayer().getData().getPoints());

        client.send(CurrenciesMessageComposer.compose(currencies));
        currencies.clear();

        client.send(WelcomeUserMessageComposer.compose());
        client.send(AllowancesMessageComposer.compose(client.getPlayer().getData().getRank()));

        client.send(AdvancedAlertMessageComposer.compose("Comet Server", "Test alert!"));

        /*


        client.send(LoadFriendsMessageComposer.compose(client.getPlayer().getMessenger().getFriends()));
        client.getPlayer().getMessenger().sendStatus(true, client.getPlayer().getEntity() != null);*/
    }
}
