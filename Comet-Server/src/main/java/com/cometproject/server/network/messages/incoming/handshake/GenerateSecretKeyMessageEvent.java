package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.handshake.SecretKeyMessageComposer;
import com.cometproject.server.protocol.codec.EncryptionDecoder;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GenerateSecretKeyMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) {
        String publicKey = msg.readString();

        String plaintextKey = NetworkManager.getInstance().getRSA().decrypt(publicKey).replace(Character.toString((char) 0), "");

        String P = client.getDiffieHellman().getPublicKey().toString();
        String encP = NetworkManager.getInstance().getRSA().sign(P);

        client.send(new SecretKeyMessageComposer(encP));

        client.getDiffieHellman().generateSharedKey(plaintextKey);
        byte[] sharedKey = client.getDiffieHellman().getSharedKey().toByteArray();

        client.getChannel().pipeline().addBefore("messageDecoder", "encryptionDecoder", new EncryptionDecoder(sharedKey));
    }
}
