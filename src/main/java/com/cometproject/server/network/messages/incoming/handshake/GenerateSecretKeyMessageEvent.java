package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.SecretKeyMessageComposer;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GenerateSecretKeyMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        String cipherPublicKey = msg.readString();

        if(!Comet.getServer().getNetwork().getEncryption().initialize(client, cipherPublicKey)) {
            client.send(AdvancedAlertMessageComposer.compose("Internal Server Error", "There was an error logging you in, please try again!"));
            return;
        }

        client.send(SecretKeyMessageComposer.compose(Comet.getServer().getNetwork().getEncryption().getPublicKey()));
        //client.send(SecretKeyMessageComposer.compose("21537332639399204031623654620231596559338920113974249280766895854583189569023"));
    }
}
