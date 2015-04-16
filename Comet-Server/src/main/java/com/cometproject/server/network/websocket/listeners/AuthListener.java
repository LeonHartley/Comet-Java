package com.cometproject.server.network.websocket.listeners;

import com.cometproject.server.network.websocket.listeners.types.AuthenticationRequest;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;

import java.util.HashMap;

public class AuthListener implements DataListener<AuthenticationRequest> {
    public static final String EVENT_NAME = "messengerAuthRequest";
    private static final String AUTH_OK = "authok";

    @Override
    public void onData(SocketIOClient socketIOClient, AuthenticationRequest authenticationRequest, AckRequest ackRequest) throws Exception {
        System.out.println(authenticationRequest.getTicket());

        socketIOClient.sendJsonObject(new HashMap<String, Object>() {{
            put("id", AUTH_OK);
        }});
    }
}
